/*class ChetniDialog extends React.Component {
    constructor(props) {
        super(props);
        this.state = { chetni: null };
    }
    fetchChetni = async ()=>{
        const dat = await fet("miejsca/"+miejsceId+"/chetni_uczestnicy");
        this.setState({chetni:dat})
    }
    
    render() {
        this.fetchChetni();
        const listChetni = this.state.chetni.map(({daneRodzica,daneDziecka})=>(
            <td>{daneRodzica.imie}</td>,
                <td>{daneRodzica.nazwisko}</td>,
                <td>{daneRodzica.telefon}</td>,
                <td>{daneRodzica.email}</td>,
                <td>{daneDziecka.imie}</td>,
                <td>{daneDziecka.nazwisko}</td>,
                <td>{daneDziecka.imie}</td>
        ));
        return (
            <div>
                <table>
                    <tr>
                        <th colspan="4">Opiekun</th>
                        <th colSpan="2">Dziecko</th>
                        <th colSpan="2">Ogólne</th>
                    </tr>
                    <tr>
                        <th>Imie</th>
                        <th>Nazwisko</th>
                        <th>Telefon</th>
                        <th>Email</th>
                        <th>Imie</th>
                        <th>Nazwisko</th>
                        <th>Akcja</th>
                    </tr>
                </table>
            </div>
        );
    }
}*/

class ParType {
  static TEXT = 1;
  static DATA_PARENT = 2;
  static DATA_CHILD = 3;
  static GROUPS_CHOOSER = 4;
  static FEE_SCHEDULE = 5;
  static INDIVIDUAL_FEE_CHECKBOX = 6;
  static CUSTOM_CHECKBOX = 7;
  static SMS_CODE = 8;
  static EMAIL_CONTENT = 9;
  static SMS_CONTENT = 10;
  static PARTICIPANTS = 12;
}
function MikrotabelkaDaneUczestnika({
  dane,
  title
}) {
  const listChetni = Object.entries(dane).map(([key, value], index) => {
    if (key === "id") {
      return;
    }
    return /*#__PURE__*/React.createElement("tr", {
      key: index
    }, /*#__PURE__*/React.createElement("td", null, key), /*#__PURE__*/React.createElement("td", null, value));
  });
  return /*#__PURE__*/React.createElement("table", null, /*#__PURE__*/React.createElement("tbody", null, /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("th", {
    colSpan: "2"
  }, title)), listChetni));
}
function MikrotabelkaWypParagraf({
  dane
}) {
  const type = dane["type"];
  const data = dane["data"];
  const par = dane["paragraf"];
  function ChoosedGroups({
    data
  }) {
    if (data["groups"] == null || data["groups"].length === 0) {
      return null;
    }
    const grupki = data["groupsEntities"].map((grupka, i) => /*#__PURE__*/React.createElement("tr", {
      key: i
    }, /*#__PURE__*/React.createElement("td", null, grupka["nazwa"])));
    return /*#__PURE__*/React.createElement("table", null, /*#__PURE__*/React.createElement("tbody", null, /*#__PURE__*/React.createElement("tr", {
      key: "0"
    }, /*#__PURE__*/React.createElement("th", null, "Propozycje grup")), grupki));
  }
  function CustomCheckbox({
    data,
    par
  }) {
    const checked = data["checked"];
    const parData = par["data"];
    const required = parData["required"];
    const name = par["name"];
    return /*#__PURE__*/React.createElement("table", null, /*#__PURE__*/React.createElement("tbody", null, /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("th", null, "Checkbox")), /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("th", null, name)), /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("th", null, required ? "wymagany" : "niewymagany")), /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null, checked ? "zaznaczony!" : "niezaznaczony!"))));
  }
  if (type === ParType.GROUPS_CHOOSER) {
    return /*#__PURE__*/React.createElement(ChoosedGroups, {
      data: data
    });
  } else if (type === ParType.CUSTOM_CHECKBOX) {
    return /*#__PURE__*/React.createElement(CustomCheckbox, {
      par: par,
      data: data
    });
  }
}
function ChetnySzczegoly({
  uczestnik
}) {
  let [umowa, setUmowa] = React.useState(null);
  let [error, setError] = React.useState(null);
  const fetchUmowa = () => {
    fet("uczestnicy/" + uczestnik["id"] + "/umowy").then(umowy_arr => {
      //const umowy_arr = hat_embedded(packet);
      /*fet(hat_link(umowy_arr[0],"wypelnioneParagrafy")).then(packet=> {
          setWypPar(packet);
      }).catch(err=>setError(err));*/
      if (umowy_arr == null || umowy_arr.length === 0) {
        return;
      }
      setUmowa(umowy_arr[0]);
    }).catch(err => setError(err));
  };
  if (error != null) {
    return /*#__PURE__*/React.createElement("span", null, "error!");
  }
  if (umowa == null) {
    fetchUmowa();
    return /*#__PURE__*/React.createElement("span", null, "wczytuj\u0119...");
  }
  const openSave = (miejsce, uczestnik) => {
    let box = Dialog.build().inner( /*#__PURE__*/React.createElement(PrzydzielUczestnika, {
      miejsce: miejsce,
      uczestnik: uczestnik
    })).centered(true).title("zapisywanie uczestnika").draw();
  };
  function InneDaneKandydata({
    wypPar
  }) {
    if (wypPar == null) {
      return null;
    }
    const map = wypPar.map((par, i) => /*#__PURE__*/React.createElement(MikrotabelkaWypParagraf, {
      key: i,
      dane: par
    }));
    return map;
  }
  const wyppar = umowa["wypelnioneParagrafy"];
  const formularz = umowa["formularz"];
  return /*#__PURE__*/React.createElement("div", {
    className: "box_chetni"
  }, /*#__PURE__*/React.createElement("div", {
    className: "dane"
  }, /*#__PURE__*/React.createElement(MikrotabelkaDaneUczestnika, {
    title: "Dane rodzica",
    dane: uczestnik["daneRodzica"]
  }), /*#__PURE__*/React.createElement(MikrotabelkaDaneUczestnika, {
    title: "Dane dziecka",
    dane: uczestnik["daneDziecka"]
  }), /*#__PURE__*/React.createElement(InneDaneKandydata, {
    wypPar: wyppar
  })), /*#__PURE__*/React.createElement("div", {
    className: "przyciski"
  }, /*#__PURE__*/React.createElement("input", {
    onClick: () => openSave(formularz["miejsce"], uczestnik),
    type: "button",
    value: "zapisz na zaj\u0119cia"
  }), /*#__PURE__*/React.createElement("input", {
    type: "button",
    value: "ukryj zg\u0142oszenie"
  }), /*#__PURE__*/React.createElement("input", {
    type: "button",
    value: "przejd\u017A do konwersacji SMS"
  })));
}
export default function ChetniDialog({
  miejsceId
}) {
  //const chetni = await fet("miejsca/"+miejsceId+"/chetni_uczestnicy");
  let [chetni, setChetni] = React.useState(null);
  let [error, setError] = React.useState(null);
  const fetchChetni = () => {
    fet("miejsca/" + miejsceId + "/chetni_uczestnicy").then(res => setChetni(res)).catch(err => setError(err));
  };
  const openDetails = uczestnik => {
    let box = Dialog.build().inner( /*#__PURE__*/React.createElement(ChetnySzczegoly, {
      uczestnik: uczestnik
    })).centered(true).title("szczegóły uczestnika").draw();
  };
  if (error != null) {
    return /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("span", null, "Wyst\u0105pi\u0142 b\u0142\u0105d!"), /*#__PURE__*/React.createElement("span", null, error));
  }
  if (chetni == null) {
    fetchChetni();
    return /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("span", null, "Wczytywanie..."));
  }
  if (chetni.length === 0) {
    fetchChetni();
    return /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("span", null, "Brak ch\u0119tnych!"));
  }
  const listChetni = chetni.map((chetny, i) => {
    const daneRodzica = chetny["daneRodzica"];
    const daneDziecka = chetny["daneDziecka"];
    return /*#__PURE__*/React.createElement("tr", {
      key: i
    }, /*#__PURE__*/React.createElement("td", null, daneRodzica.imie), /*#__PURE__*/React.createElement("td", null, daneRodzica.nazwisko), /*#__PURE__*/React.createElement("td", null, daneRodzica.telefon), /*#__PURE__*/React.createElement("td", null, daneRodzica.email), /*#__PURE__*/React.createElement("td", null, daneDziecka.imie), /*#__PURE__*/React.createElement("td", null, daneDziecka.nazwisko), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("input", {
      onClick: () => openDetails(chetny),
      type: "button",
      value: "wybierz"
    })));
  });
  return /*#__PURE__*/React.createElement("div", {
    className: "chetni_scroller"
  }, /*#__PURE__*/React.createElement("table", null, /*#__PURE__*/React.createElement("tbody", null, /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("th", {
    colSpan: "4"
  }, "Opiekun"), /*#__PURE__*/React.createElement("th", {
    colSpan: "2"
  }, "Dziecko"), /*#__PURE__*/React.createElement("th", {
    colSpan: "2"
  }, "Og\xF3lne")), /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("th", null, "Imie"), /*#__PURE__*/React.createElement("th", null, "Nazwisko"), /*#__PURE__*/React.createElement("th", null, "Telefon"), /*#__PURE__*/React.createElement("th", null, "Email"), /*#__PURE__*/React.createElement("th", null, "Imie"), /*#__PURE__*/React.createElement("th", null, "Nazwisko"), /*#__PURE__*/React.createElement("th", null, "Akcja")), listChetni)));
}
function SelectGrupaTaneczna({
  miejsce,
  def
}) {
  let [grupy, setGrupy] = React.useState(null);
  let [error, setError] = React.useState(null);
  const fetchGrupy = () => {
    fet(`miejsca/${miejsce}/grupy`).then(packet => {
      setGrupy(hat_embedded(packet));
    }).catch(err => setError(err));
  };
  if (grupy == null) {
    fetchGrupy();
    return /*#__PURE__*/React.createElement("span", null, "wczytuje...");
  }
  const gr = grupy.map((grupa, i) => /*#__PURE__*/React.createElement("option", {
    key: i,
    value: grupa["id"]
  }, grupa["nazwa"]));
  return /*#__PURE__*/React.createElement("select", null, gr);
}
function PrzydzielUczestnika({
  miejsce,
  uczestnik
}) {
  return /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("table", null, /*#__PURE__*/React.createElement("tbody", null, /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null, "grupa taneczna"), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement(SelectGrupaTaneczna, {
    miejsce: miejsce
  }))))));
}
//# sourceMappingURL=chetni_dialog.js.map