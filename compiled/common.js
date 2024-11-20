let cities = null;
let AUTH_KEY = "";
let modular = {
  panels: {}
};
window.onload = function () {
  console.log("Meow!");
  const query = window.location.search.substring(1);
  const qs = parse_query_string(query);
  if ('dev' in qs) {
    switchToDevServerURL();
  }
  setTimeout(function () {
    recalculateElements();
    recalculateTopRightStats();
    sprawdzZmiany();
  }, 50);
  fet("miasta").then(res => {
    cities = res;
    cities.byid = function (id) {
      return cities.find(function (element) {
        return element["id"] == id;
      });
    };
  }).catch(err => {
    Dialog.build().inner("Niepowodzenie wczytywania danych: miasta<br>program może nie działać prawidłowo").title("Wystąpił problem").draw();
  });
  modular.showMessagesPanel();
  window.isMobile = window.innerHeight > window.innerWidth;
};
function zapisy(miejsceId) {
  modular.openChetni(miejsceId);
}
function wiadomosci() {
  //const uid = zaznaczo
  const ucz = getZaznaczeniUczestnicy();
  if (ucz == null || ucz == '') {
    return;
  }
  modular.openWiadomosci(parseInt(ucz));
}
function insertAfter(newNode, existingNode) {
  existingNode.parentNode.insertBefore(newNode, existingNode.nextSibling);
}
function findPlaceDialog() {
  let div = crEl("div");
  let box = Dialog.build().inner(div).centered(false).draw(true);
  let t = div.crEl("div", null, "Wyszukaj miejsce w bazie danych<br>po nazwie lub adresie<br>Kliknij wiersz aby przejść");
  t.style.textAlign = "center";
  let tab = div.crEl("table", "findPlaceTable");
  let cells = [];
  let inp = tab.crEl("tr").crEl("td").crEl("input/text");
  inp.placeholder = "Wyszukaj...";
  inp.onchange = function () {
    fet("miejsca/search", {
      method: "POST",
      body: JSON.stringify({
        name: inp.value,
        season: getActiveSeason()
      })
    }).then(res => {
      for (let i = 0; i < 10; i++) {
        let cell = cells[i];
        if (res != null && res[i] != null) {
          let row = res[i];
          cell.innerHTML = row.name + " " + row.address;
          cell.onclick = function () {
            zmienAktywneMiejsce(row["id"], null);
          };
          cell.classList.add("wyszukiwanieUczestnikaWyniki");
        } else {
          cell.innerHTML = "";
          cell.onclick = null;
          cell.classList.remove("wyszukiwanieUczestnikaWyniki");
        }
      }
    });
  };
  inp.oninput = inp.onchange;
  inp.onpaste = inp.onchange;
  setTimeout(() => {
    inp.focus();
  }, 500);
  for (let i = 0; i < 10; i++) {
    let row = tab.crEl("tr");
    let cell = row.crEl("td");
    cells.push(cell);
  }
}
function wyswietlPopupWyszukiwanieUczestnikow() {
  let r = '';
  r += '<center>';
  r += '	<b>Wyszukaj uczestnika w bazie danych</b></br>';
  r += '	<b>po imieniu, nazwisku lub telefonie.</b></br>';
  r += '	<b>Kliknij na uczestnika aby przejść do miejsca.</b></br>';
  r += '	</br>';
  r += '	<div style="">';
  r += '		<table id="szukajUczestnikowTable">';
  r += '		</table>';
  r += '	</div>';
  r += '</center>';
  wywalPopup(r);

  //pola wyboru znalezionych
  let cells = [];
  for (let i = 0; i < 10; i++) {
    let cell = document.createElement('td');
    cell.style = "width:300px;height:28px;";
    let row = document.createElement('tr');
    row.appendChild(cell);
    cells.push(cell);
    document.getElementById("szukajUczestnikowTable").appendChild(row);
  }

  //szukajka
  let input = document.createElement('input');
  let cell = document.createElement('td');
  let row = document.createElement('tr');
  input.placeholder = "Wyszukaj...";
  input.style = "width:100%;height:100%;";
  input.onchange = function () {
    szukajUczestnika("wyszukiwanieUczestnikaPoImieniuNazwiskuTelefonie", input.value, cells, function (el, info) {
      if (info == null) {
        if (el.classList.contains('wyszukiwanieUczestnikaWyniki')) {
          el.classList.remove('wyszukiwanieUczestnikaWyniki');
        }
        el.innerHTML = "";
      } else {
        if (!el.classList.contains('wyszukiwanieUczestnikaWyniki')) {
          el.classList.add('wyszukiwanieUczestnikaWyniki');
        }
        el.innerHTML = "[" + info.miejsceNazwa + "] " + info.imie + " " + info.nazwisko;
      }
    }, function (el, info) {
      let uczestnikId = info['id'];
      zmienAktywneMiejsce(info['miejsce'], function () {
        wybierzUczestnika(uczestnikId);
        centerUserRow(uczestnikId);
      });
    });
  };
  input.oninput = input.onchange;
  input.onpaste = input.onchange;
  cell.style = "width:300px;height:28px;";
  cell.appendChild(input);
  row.appendChild(cell);
  //document.getElementById("szukajUczestnikowTable").appendChild(row);
  document.getElementById("szukajUczestnikowTable").insertBefore(row, document.getElementById("szukajUczestnikowTable").firstChild);
  input.focus();
}
function spin(element, boo) {
  if (boo) {
    element.classList.add("spinning");
  } else {
    element.classList.remove("spinning");
  }
}
let objecttransfers = [];
function formularze() {
  let miejsceId = getAktywneMiejsce();
  fet("miejsca/" + miejsceId, {
    method: "GET"
  }).then(miejsce => {
    let el = crEl("div", "set_password");
    el.style.minWidth = "190px";
    let box = Dialog.build().inner(el).centered(true).title("Edycja formularzy").draw();
    box.element.style.minWidth = "600px";
    box.element.style.minHeight = "400px";
    const center_div = el.crEl("div");
    center_div.style.textAlign = "center";
    center_div.style.marginBottom = "5px";
    center_div.crEl("span", null, "Miejsce: <i>" + miejsce['nazwa'] + "</i> <br>");
    center_div.crEl("span", null, "formularze");
    let formuls = el.crEl("div", "formularze");
    const loadForms = function (succ, fail) {
      //"miejsca/"+miejsceId+"/formularze"//"?projection=linki"
      req([miejsce, "formularze"], "GET/MBED", null, forms => {
        formuls.innerHTML = "";
        if (forms == null) {
          return;
        }
        forms.forEach(form => {
          //lista popupów/formularzy dla miejsca
          let div = formuls.crEl("div", "form");
          div.crEl("span", null, form['name']);
          let hh = div.crEl("span", "clickable", "aktywuj");
          hh.style.margin.left = "5px";
          hh.style.position = "absolute";
          hh.style.top = "2px";
          hh.style.right = "5px";
          hh.onclick = function (ev) {};
          let paragrafy = div.crEl("div", "paragrafy");

          //const popupId = parseInt(form['id']);

          function applyParagrafDatatransferData(element, object) {
            element.ondragstart = function drag(ev) {
              //0->1000
              let i = Math.floor(Math.random() * 1001);
              objecttransfers[i] = element;
              ev.dataTransfer.setData("div", i + "");
              ev.dataTransfer.setData("paragraf", RESTObjectToRelativeSelfLink(object));
              ev.dataTransfer.setData("formularz", RESTObjectToRelativeSelfLink(form));
            };
          }
          div.ondragover = function (ev) {
            ev.preventDefault();
          };
          div.ondrop = ev => {
            ev.preventDefault();
            let stary_formularz_link = ev.dataTransfer.getData("formularz");
            let stary_paragraf_link = ev.dataTransfer.getData("paragraf");
            let i = parseInt(ev.dataTransfer.getData("div"));
            if (paragrafy.childElementCount === 0) {
              paragrafy.innerHTML = ""; //remove [Pusto]
            }

            let elem = objecttransfers[i];
            //debugger;
            let oldparent = elem.parentElement;
            if (stary_formularz_link === RESTObjectToRelativeSelfLink(form)) {
              return;
            }
            if (oldparent.childElementCount === 0) {
              oldparent.innerHTML = "[Pusto]"; //add [Pusto]
            }

            objecttransfers.splice(i, 1);

            //przerzucanie paragrafów POMIĘDZY formularzami
            req(stary_paragraf_link, "PATCH", {
              "formularz": form,
              "ord": 99
            }, data => {
              paragrafy.appendChild(elem);
            }, err => {
              console.error(err);
              loadForms();
            });
          };

          //paragrafy danego popupu
          let parags = form['paragrafy'];
          req([form, "paragrafy"], "GET/MBED", null, parags => {
            if (parags == null || parags.length == 0) {
              const pu = paragrafy.crEl("div");
              pu.innerHTML = "[Pusto]";
              pu.style.textAlign = "center";
              pu.style.marginBottom = "3px";
            }
            parags.forEach(par => {
              let d = paragrafy.crEl("div", "paragraf");
              d.ondragover = function (ev) {
                ev.preventDefault();
              };
              d.ondrop = function (ev) {
                ev.preventDefault();
                const dropped_formularz_link = ev.dataTransfer.getData("formularz");
                const dropped_paragraf_link = ev.dataTransfer.getData("paragraf");
                let i = parseInt(ev.dataTransfer.getData("div"));
                let elem = objecttransfers[i];
                if (dropped_formularz_link !== RESTObjectToRelativeSelfLink(form)) {
                  return;
                }
                //"paragrafy/"+droppedParagrafId+"/order_after/"+par['id']
                req([dropped_paragraf_link, "order_after", par["_id"]], "GET", null, r => {
                  applyParagrafDatatransferData(elem, r);
                  insertAfter(elem, d);
                });
              };
              let pen = d.crEl("i", ["fa-solid", "fa-pen", "fa-xs"]);
              pen.style.paddingTop = "7px";
              pen.onclick = function (ev) {
                //edycja paragrafu
                let el = crEl("div", "flex_col_dial");
                let dialog = Dialog.build().inner(el).parent(ev.target).title("edycja paragrafu").draw();
                let inp = el.crEl("input/text");
                inp.placeholder = "Nazwa...";
                inp.value = par["name"];
                let jsondata = null;
                try {
                  jsondata = JSON.parse(par["data"]);
                } catch (e) {
                  console.error(e);
                }
                let type = parseInt(par["type"]);
                let sel = el.crEl("select");
                let additional = el.crEl("div", "parTypeAddsCont");
                parTypes.forEach((value, key, map) => {
                  if (value == null) return;
                  let opt = sel.crEl("option", null, value);
                  opt.value = key; //for later
                  if (type === key) {
                    opt.selected = "true";
                  }
                });
                let gen = buildParagraphAdditionalOpts(type, additional, jsondata, miejsceId);
                let b = el.crEl("input/button", null, "Zapisz");
                b.onclick = function () {
                  let s = inp.value;
                  let type = sel.value;
                  let data = gen["f"]();
                  let patch = {
                    "type": type,
                    "data": JSON.stringify(data)
                  };
                  if (inp.value !== par["name"]) {
                    patch["name"] = inp.value;
                  }
                  req(hat_link(par, "self"), "PATCH", patch, () => {
                    dialog.close();
                    loadForms();
                  }, null);
                };
                let cb = el.crEl("input/button", null, "Usuń");
                cb.onclick = function () {
                  req("paragrafy/" + par["id"], "DELETE", null, () => {
                    dialog.close();
                    loadForms();
                  }, null);
                };
              };
              d.crEl("span", "paragraf_nazwa", parTypes.get(parseInt(par['type'])));
              d.crEl("span", "paragraf_tytul", par['name']);
              d.draggable = true;
              applyParagrafDatatransferData(d, par);
            });
          }, err => {});
          let botBtns = div.crEl("div", "formBtns");
          let pen = botBtns.crEl("i", ["fa-solid", "fa-pen", "fa-xs"]);
          pen.style.paddingTop = "7px";
          pen.onclick = function (ev) {
            //edycja formularza
            let el = crEl("div", "edit_form");
            let dialog = Dialog.build().inner(el).parent(ev.target).title("edycja formularza").draw();
            let inp = el.crEl("input/text", null, form['name']);
            let b = el.crEl("input/button", null, "Zapisz zmiany");
            b.onclick = function () {
              let s = inp.value;
              req(form, "PATCH", "name=" + s, () => {
                dialog.close();
                loadForms();
              }, null);
            };
            let c = el.crEl("input/button", null, "Usuń formularz");
            c.onclick = function () {
              request(form, "DELETE", null, () => {
                dialog.close();
                loadForms();
              }, null);
            };
          };

          //dodawanie paragrafu
          let ad = botBtns.crEl("i", ["fa-solid", "fa-plus"]);
          ad.onclick = function (ev) {
            let el = crEl("div", "flex_col_dial");
            let dialog = Dialog.build().inner(el).parent(ev.target).title("dodawanie paragrafu").draw();
            let inp = el.crEl("input/text");
            inp.placeholder = "Nazwa...";
            let sel = el.crEl("select");
            let additional = el.crEl("div", "parTypeAddsCont");
            parTypes.forEach((value, key, map) => {
              if (value == null) return;
              let opt = sel.crEl("option", null, value);
              opt.value = key; //for later
            });

            //let map = [];
            let type = 1;
            let b = el.crEl("input/button", null, "Utwórz");
            const func = ev => {
              type = parseInt(sel.value);
              additional.innerHTML = "";
              let provdata = {};
              let gen = buildParagraphAdditionalOpts(type, additional, provdata, miejsceId);
              b.onclick = function () {
                let s = inp.value;
                let data = gen == null || gen["f"] == null ? null : gen["f"]();
                const patch = {
                  "formularz": hat_id(hat_link(form, "self")),
                  "type": type,
                  "name": s,
                  "data": data
                };
                req("paragrafy", "POST", patch, () => {
                  dialog.close();
                  loadForms();
                }, null);
              };
            };
            sel.onchange = func;
            func();
          };
        });
        if (succ != null) succ();
      }, fail);
    };

    //ODŚWIEŻ LISTĘ FORMULARZY DLA MIEJSCA
    let refr = center_div.crEl("i", ["fa-solid", "fa-arrows-rotate"]);
    refr.onclick = () => {
      /*box.close();
      formularze();*/
      spin(refr, true);
      loadForms(() => {
        spin(refr, false);
      });
    };

    //DODAJ NOWY FORMULARZ DLA MIEJSCA
    let adder = center_div.crEl("i", ["fa-solid", "fa-plus"]);
    adder.onclick = ev => {
      let el = crEl("div", "new_form");
      let dialog = Dialog.build().inner(el).parent(ev.target).title("nowy formularz").draw();
      let inp = el.crEl("input/text");
      inp.placeholder = "Nazwa...";
      let b = el.crEl("input/button", null, "Dodaj");
      b.onclick = function () {
        let s = inp.value;
        req("formularze", "PUT", {
          "name": s,
          "miejsce": "miejsca/" + miejsceId
        }, function () {
          dialog.close();
          loadForms();
        }, null);
      };
    };
    loadForms();
  });
}
function buildParagraphAdditionalOpts(type, additional, provdata, miejsceId) {
  let gen = {};
  let fill = function (el, s) {
    //return s == null ? "" : s;
    if (s == null) {
      return;
    }
    if (el.tagName === "INPUT" && el.type === "checkbox") {
      el.checked = s;
    } else if (el.tagName === "INPUT" && (el.type === "number" || el.type === "text")) {
      el.value = s;
    } else if (el.tagName === "TEXTAREA") {
      el.value = s;
    } else if (el.tagName === "SELECT") {
      el.value = s;
    }
  };
  switch (type) {
    case 1:
      let area = additional.crEl("textarea");
      area.placeholder = "Dowolny tekst...\nZmienne: [DATA] [KONTO]";
      fill(area, provdata["content"]);
      gen["f"] = function (area) {
        let data = {};
        data["content"] = area.value;
        return data;
      }.bind(this, area);
      break;
    case 4:
      fet("miejsca/" + miejsceId + "/grupy").then(res => {
        let grupy = hat_embedded(res);
        let div = additional.crEl("div");
        let showh = div.crEl("input/checkbox");
        fill(showh, provdata["showhours"]);
        div.crEl("span", null, "Wyświetl godziny");
        additional.crEl("span", null, "Dostępne grupy:");
        let m = new Map();
        grupy.forEach(d => {
          let div = additional.crEl("div");
          const ch = div.crEl("input/checkbox");
          m.set(d['id'], ch);
          ch.checked = provdata["groups"] != null && provdata["groups"].includes(d['id']);
          div.crEl("span", null, d['nazwa']);
        });
        gen['f'] = function (showh, m) {
          let data = {};
          data["showhours"] = showh.checked;
          data["groups"] = [];
          m.forEach((v, k) => {
            if (v.checked) {
              data["groups"].push(k);
            }
          });
          return data;
        }.bind(this, showh, m);
      });
      break;
    case 5:
      //harmonogram opłat
      fet("miejsca/" + miejsceId + "/grupy").then(res => {
        let grupy = hat_embedded(res);
        debugger;
        additional.crEl("span", null, "wybór grupy...");
        let sel = additional.crEl("select");
        grupy.forEach(d => {
          let opt = sel.crEl("option", null, d['nazwa']);
          opt.value = hat_id(hat_link(d, "self")); //for later
        });

        fill(sel, provdata["group"]);
        gen["f"] = function (sel) {
          let data = {};
          data["group"] = sel.value;
          return data;
        }.bind(this, sel);
      });
      break;
    case 6:
      //płatność indywidualna
      let div = additional.crEl("div");
      div.crEl("span", null, "Cena za 1 zajęcia: ");
      let inp = div.crEl("input/number", null, "0");
      inp.style.width = "70px";
      fill(inp, provdata["price"]);
      gen["f"] = function (inp) {
        let data = {};
        data["price"] = parseInt(inp.value);
        return data;
      }.bind(this, inp);
      break;
    case 7:
      //do akceptacji
      let fil = additional.crEl("div");
      let i = fil.crEl("input/checkbox", null);
      fil.crEl("span", null, "Obowiązkowy");
      let ar = additional.crEl("textarea");
      ar.placeholder = "Dowolny tekst...\nZmienne: [DATA] [KONTO]";
      fill(i, provdata["required"]);
      fill(ar, provdata["content"]);
      gen["f"] = function (area, check) {
        let data = {};
        data["content"] = area.value;
        data["required"] = check.checked;
        return data;
      }.bind(this, ar, i);
      break;
    case 8:
      //kod sms
      {
        let div = additional.crEl("div");
        div.crEl("span", null, "Treść na przycisku: ");
        let inp = div.crEl("input/text", null, "Wyślij kod");
        inp.style.width = "100px";
        fill(inp, provdata["buttontext"]);
        gen["f"] = function (te) {
          let data = {};
          data["buttontext"] = te.value;
          return data;
        }.bind(this, inp);
      }
      break;
    case 9:
      //MAIL
      {
        let ar = additional.crEl("textarea");
        ar.placeholder = "Treść wysyłanego e-maila";
        additional.crEl("span", null, "Dostępne zmienne:<br>" + "[dziecko-imie][dziecko-nazw][dziecko-tel][dziecko-email]<br>" + "[rodzic-imie][rodzic-nazw][rodzic-tel][rodzic-email]<br>" + "[znak][data]");
        let div = additional.crEl("div");
        let chkbx = div.crEl("input/checkbox");
        div.crEl("span", null, "Załącz umowę");
        fill(chkbx, provdata["attach"]);
        fill(ar, provdata["content"]);
        gen["f"] = function (area, ckbx) {
          let data = {};
          data["content"] = area.value;
          data["attach"] = ckbx.checked;
          return data;
        }.bind(this, ar, chkbx);
      }
      break;
    case 10:
      //SMS
      {
        let ar = additional.crEl("textarea");
        ar.placeholder = "Treść wysyłanego sms";
        additional.crEl("span", null, "Dostępne zmienne:<br>" + "[dziecko-imie][dziecko-nazw][dziecko-tel][dziecko-email]<br>" + "[rodzic-imie][rodzic-nazw][rodzic-tel][rodzic-email]<br>" + "[znak][data]");
        let div = additional.crEl("div");
        fill(ar, provdata["content"]);
        gen["f"] = function (area) {
          let data = {};
          data["content"] = area.value;
          return data;
        }.bind(this, ar);
      }
    case 12:
      //lista zapisanych
      {
        fet("miejsca/" + miejsceId + "/grupy").then(res => {
          let grupy = hat_embedded(res);
          let div = additional.crEl("div");
          let showh = div.crEl("input/checkbox");
          div.crEl("span", null, "Wyświetl godziny");
          additional.crEl("span", null, "Widoczne grupy:");
          let m = new Map();
          grupy.forEach(d => {
            let div = additional.crEl("div");
            /*map["gr-"+d['id']] = */
            const ch = div.crEl("input/checkbox");
            m.set(d['id'], ch);
            ch.checked = provdata["groups"] != null && provdata["groups"].includes(d['id']);
            div.crEl("span", null, d['nazwa']);
          });
          fill(showh, provdata["showhours"]);
          gen["f"] = function (m, showh) {
            let data = {};
            data["showhours"] = showh.checked;
            data["groups"] = [];
            m.forEach((v, k) => {
              if (v.checked) {
                data["groups"].push(k);
              }
            });
            return data;
          }.bind(this, m, showh);
        });
      }
    default:
      gen["f"] = function () {
        return {};
      };
      break;
  }
  return gen;
}
const parTypes = new Map([[0, null], [1, "tekst"], [2, "dane rodzica"], [3, "dane dziecka"], [4, "wybór grupy"], [5, "harmonogram opłat"], [6, "płatność indywidualna"], [7, "checkbox z tekstem"], [8, "kod sms"], [9, "treść maila"], [10, "treść sms"], [11, null], [12, "lista zapisanych"]]);
//# sourceMappingURL=common.js.map