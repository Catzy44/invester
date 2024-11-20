import { Dialog, DialogYesNo, showDialog } from "./dialog.js";
import { DialogAttenderManupulatePayments } from "./dialog_attender_manipulate_payments";
import { dzien, EditionsMenuBtn, GroupAttendersInfoTableRows, PlaceType } from "./panel_platnosci";
import { kon, zebra } from "./main";
import { jsx as ___EmotionJSX } from "@emotion/react";
const {
  useRef,
  useEffect,
  useState,
  createRef,
  useCallback,
  cloneElement
} = React;
class PanelSMSWew extends React.Component {
  state = {
    placeInfo: null,
    instrInfo: null,
    error: null,
    selectedAttenders: [],
    selectedGroups: [],
    smsTemplates: null,
    textareaText: "",
    filters: {
      age: {
        enabled: false,
        from: 0,
        to: 0
      },
      sex: {
        enabled: false,
        value: 0
      }
    }
  };
  constructor(props) {
    super(props);
    this.miejsceId = modular.selectors.place;
    //this.state.selectedAttender = modular.selectors.attender

    fet(`miejsca/${this.miejsceId}?projection=withGroups`).then(ret => {
      fet(`instruktorzy/${ret['instructor']}`, ret => {
        this.setState({
          ...this.state,
          instrInfo: ret
        });
      }, ret => {
        this.setState({
          ...this.state,
          error: ret
        });
      });
      this.setState({
        ...this.state,
        placeInfo: ret
      });
    }).catch(ret => {
      this.setState({
        ...this.state,
        error: ret
      });
    });
    zebra("sms_templates/type/1", kon(this, "smsTemplates"), null);
  }
  setSelectedAttender = (att, selected) => {
    /*modular.selectors.attender = att
    this.setState(state=>({...state,selectedAttender:att}))*/

    if (selected) {
      this.setState(state => ({
        ...state,
        selectedAttenders: state.selectedAttenders.concat([att])
      }));
    } else {
      this.setState(state => ({
        ...state,
        selectedAttenders: state.selectedAttenders.filter(a => a !== att)
      }));
    }
  };
  setSelectedGroup = (att, selected) => {
    //this.setState(state=>({...state,selectedGroup:att}))
    //this.setState(state=>({...state,selectedGroup:att}))
    if (selected) {
      this.setState(state => ({
        ...state,
        selectedGroups: state.selectedGroups.concat([att])
      }));
    } else {
      this.setState(state => ({
        ...state,
        selectedGroups: state.selectedGroups.filter(a => a !== att)
      }));
    }
  };
  render() {
    if (this.state.error != null) {
      return /*#__PURE__*/React.createElement("span", null, "B\u0142\u0105d! ", this.state.error);
    }
    if (this.state.placeInfo == null) {
      return /*#__PURE__*/React.createElement("span", null, "Wczytywanie...");
    }
    if (this.state.smsTemplates == null) {
      return /*#__PURE__*/React.createElement("span", null, "Wczytywanie...");
    }
    const placeInfo = this.state.placeInfo;
    const placeType = placeInfo.type;
    const instrInfo = this.state.instrInfo;
    return /*#__PURE__*/React.createElement("div", {
      className: "panel-payments"
    }, /*#__PURE__*/React.createElement("div", {
      className: "payments-top"
    }, /*#__PURE__*/React.createElement("div", {
      className: "payments-info"
    }, /*#__PURE__*/React.createElement("div", {
      className: "info"
    }, /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("h4", null, placeInfo.name), /*#__PURE__*/React.createElement("span", null, placeInfo.address)), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("h4", null, "dyrekcja"), /*#__PURE__*/React.createElement("span", null, `${placeInfo.directorFirstname} ${placeInfo.directorLastname} ${placeInfo.phoneNum}`)), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("h4", null, "instruktor"), instrInfo == null ? null : /*#__PURE__*/React.createElement("span", null, `${instrInfo.directorFirstname} ${instrInfo.directorLastname} ${instrInfo.phoneNum}`)), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("h4", null, "dzien"), /*#__PURE__*/React.createElement("span", null, dzien(placeInfo.defaultClassWeekDay))))), /*#__PURE__*/React.createElement("div", {
      className: "payments-menu"
    }, /*#__PURE__*/React.createElement("div", {
      className: "payments-menu-editions"
    }, /*#__PURE__*/React.createElement(EditionsMenuBtn, {
      action: () => {
        modular.program.showPanel("obecnosci");
      }
    }, "powr\xF3t")))), /*#__PURE__*/React.createElement("div", {
      className: "panel-sms-bot"
    }, /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("table", {
      className: "mega-table"
    }, /*#__PURE__*/React.createElement("tbody", null, placeInfo.groups.map(({
      id
    }) => /*#__PURE__*/React.createElement(GroupAttendersInfoTableRows, {
      panel: this,
      groupId: id,
      key: "payments_info_rows_" + id
    })), /*#__PURE__*/React.createElement(GroupAttendersInfoTableRows, {
      panel: this,
      placeId: this.miejsceId,
      groupId: 0,
      key: "payments_info"
    }))), /*#__PURE__*/React.createElement("div", {
      className: "sms-send-area"
    }, /*#__PURE__*/React.createElement("textarea", {
      placeholder: "tre\u015B\u0107 wiadomo\u015Bci...",
      onChange: event => {
        this.setState({
          ...this.state,
          textareaText: event.target.value
        });
      },
      value: this.state.textareaText
    }), /*#__PURE__*/React.createElement("div", {
      className: "filters"
    }, /*#__PURE__*/React.createElement("span", null, "filtry"), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("span", null, "wiek"), /*#__PURE__*/React.createElement("div", null, "aktywny ", /*#__PURE__*/React.createElement("input", {
      type: "checkbox",
      checked: this.state.filters.age.enabled,
      onChange: event => {
        this.setState(state => {
          const checked = event.target.checked;
          state.filters.age.enabled = checked;
          return state;
        });
      }
    })), /*#__PURE__*/React.createElement("div", null, "od ", /*#__PURE__*/React.createElement("input", {
      type: "number",
      value: this.state.filters.age.from,
      onChange: event => {
        this.setState(state => {
          const value = parseInt(event.target.value);
          state.filters.age.from = value;
          return state;
        });
      }
    })), /*#__PURE__*/React.createElement("div", null, "do ", /*#__PURE__*/React.createElement("input", {
      type: "number",
      value: this.state.filters.age.to,
      onChange: event => {
        this.setState(state => {
          const value = parseInt(event.target.value);
          state.filters.age.to = value;
          return state;
        });
      }
    }))), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("span", null, "p\u0142e\u0107"), /*#__PURE__*/React.createElement("div", null, "aktywny ", /*#__PURE__*/React.createElement("input", {
      type: "checkbox",
      checked: this.state.filters.sex.enabled,
      onChange: event => {
        this.setState(state => {
          const checked = event.target.checked;
          state.filters.sex.enabled = checked;
          return state;
        });
      }
    })), /*#__PURE__*/React.createElement("select", {
      value: this.state.filters.age.value,
      onChange: event => {
        this.setState(state => {
          const sex = parseInt(event.target.value);
          state.filters.sex.value = sex;
          return state;
        });
      }
    }, /*#__PURE__*/React.createElement("option", {
      value: "0"
    }, "K"), /*#__PURE__*/React.createElement("option", {
      value: "1"
    }, "M"))))), /*#__PURE__*/React.createElement("input", {
      type: "button",
      value: "wy\u015Blij",
      onClick: () => {
        zebra("sms_groups/send", res => {
          showDialog( /*#__PURE__*/React.createElement(Dialog, {
            title: "sukces"
          }, /*#__PURE__*/React.createElement("span", null, "powodzenie!")));
        }, null, {
          method: "POST",
          body: {
            message: this.state.textareaText,
            userIds: this.state.selectedAttenders,
            filters: this.state.filters
          }
        });
      }
    })), /*#__PURE__*/React.createElement("div", {
      className: "sms-templates"
    }, this.state.smsTemplates.map(({
      id,
      type,
      nr,
      text,
      label
    }) => {
      return /*#__PURE__*/React.createElement("div", {
        key: id
      }, /*#__PURE__*/React.createElement("a", {
        onClick: () => {
          //load template
          this.setState({
            ...this.state,
            textareaText: text
          });
        },
        href: "#",
        className: "nicea"
      }, /*#__PURE__*/React.createElement("i", {
        className: "fa-solid fa-angles-left"
      })), /*#__PURE__*/React.createElement("a", {
        onClick: () => {
          //save template
          showDialog( /*#__PURE__*/React.createElement(DialogYesNo, {
            title: "zapisz szablon",
            yesClicked: () => {
              zebra("sms_templates", patchedTemplate => {
                this.setState(state => {
                  return {
                    ...state,
                    smsTemplates: state.smsTemplates.map(template => template.id === patchedTemplate.id ? patchedTemplate : template)
                  };
                });
              }, null, {
                method: "PATCH",
                body: {
                  id: id,
                  text: this.state.textareaText
                }
              });
            }
          }));
        },
        href: "#",
        className: "nicea"
      }, /*#__PURE__*/React.createElement("i", {
        className: "fas fa-save"
      })), /*#__PURE__*/React.createElement("a", {
        onClick: () => {
          //delete template
          showDialog( /*#__PURE__*/React.createElement(DialogYesNo, {
            title: "usu\u0144 szablon",
            yesClicked: () => {
              zebra(`sms_templates/${id}`, () => {
                this.setState(state => {
                  return {
                    ...state,
                    smsTemplates: state.smsTemplates.filter(template => template.id !== id)
                  };
                });
              }, null, {
                method: "DELETE"
              });
            }
          }));
        },
        href: "#",
        className: "nicea"
      }, /*#__PURE__*/React.createElement("i", {
        className: "fas fa-trash fa-xs templateNameRemove"
      })), /*#__PURE__*/React.createElement("a", {
        onClick: () => {
          const templater = this;
          function RenameTemplateDialog(props) {
            const [newName, setNewName] = useState(label);
            const dialog = useRef(null);
            return /*#__PURE__*/React.createElement(Dialog, {
              ref: dialog,
              title: "zmiana nazwy szablonu"
            }, /*#__PURE__*/React.createElement("div", {
              className: "dialog-rename-template"
            }, /*#__PURE__*/React.createElement("input", {
              type: "text",
              value: newName,
              onChange: event => {
                setNewName(event.target.value);
              }
            }), /*#__PURE__*/React.createElement("i", {
              className: "fa-solid fa-floppy-disk",
              onClick: () => {
                zebra("sms_templates", patchedTemplate => {
                  templater.setState(state => {
                    return {
                      ...state,
                      smsTemplates: state.smsTemplates.map(template => template.id === patchedTemplate.id ? patchedTemplate : template)
                    };
                  });
                  dialog.current.setClosed(true);
                }, null, {
                  method: "PATCH",
                  body: {
                    id: id,
                    label: newName
                  }
                });
              }
            })));
          }
          showDialog( /*#__PURE__*/React.createElement(RenameTemplateDialog, null));
        },
        href: "#",
        className: "nicea"
      }, /*#__PURE__*/React.createElement("i", {
        className: "fas fa-pen fa-xs templateRename"
      })), /*#__PURE__*/React.createElement("span", null, label));
    }), /*#__PURE__*/React.createElement("div", {
      className: "template-new"
    }, /*#__PURE__*/React.createElement("i", {
      onClick: () => {
        const templater = this;
        function NewTemplateDialog(props) {
          const [newName, setNewName] = useState("");
          const dialog = useRef(null);
          return /*#__PURE__*/React.createElement(Dialog, {
            ref: dialog,
            title: "nowy szablon"
          }, /*#__PURE__*/React.createElement("div", {
            className: "dialog-rename-template"
          }, /*#__PURE__*/React.createElement("input", {
            placeholder: "nazwa...",
            type: "text",
            value: newName,
            onChange: event => {
              setNewName(event.target.value);
            }
          }), /*#__PURE__*/React.createElement("i", {
            className: "fa-solid fa-floppy-disk",
            onClick: () => {
              zebra("sms_templates", addedTemplate => {
                templater.setState(state => {
                  return {
                    ...state,
                    smsTemplates: state.smsTemplates.concat([addedTemplate])
                  };
                });
                dialog.current.setClosed(true);
              }, null, {
                method: "POST",
                body: {
                  label: newName,
                  text: "",
                  type: 1
                }
              });
            }
          })));
        }
        showDialog( /*#__PURE__*/React.createElement(NewTemplateDialog, null));
      },
      className: "fa-solid fa-plus"
    })))), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement(HistoriaSMS, {
      miejsceId: placeInfo.id
    }))));
  }
}
function HistoriaSMS({
  miejsceId
}) {
  let [response, setResponse] = useState(null);
  let [error, setError] = useState(null);
  if (error != null) {
    return /*#__PURE__*/React.createElement("div", {
      className: "send_messages_dialog"
    }, /*#__PURE__*/React.createElement("span", null, "Wyst\u0105pi\u0142 b\u0142\u0105d:"), /*#__PURE__*/React.createElement("span", null, error.message));
  }
  if (response == null) {
    fet(`miejsca/${miejsceId}/sms_groups`).then(resp => {
      resp.reverse();
      setResponse(resp);
    }).catch(err => {
      setError(err);
    });
    return /*#__PURE__*/React.createElement("div", {
      className: "send_messages_dialog"
    }, /*#__PURE__*/React.createElement("span", null));
  }
  let grupy = response.map(r => {
    let sms_list = r["sms"];
    let sms_gr_id = r["id"];
    let sms_rows = sms_list.map(({
      uczestnik,
      type,
      status,
      readStatus,
      content,
      number,
      createdTimestamp
    }, idx) => {
      const {
        imie,
        nazwisko
      } = uczestnik ?? {};
      const dateExp = createdTimestamp.split("T");
      const date = dateExp[0];
      const time = dateExp[1].split("\.")[0];
      return /*#__PURE__*/React.createElement("tr", {
        key: idx
      }, /*#__PURE__*/React.createElement("td", null, date), /*#__PURE__*/React.createElement("td", null, time), /*#__PURE__*/React.createElement("td", null, imie, " ", nazwisko), /*#__PURE__*/React.createElement("td", null, content));
    });
    return /*#__PURE__*/React.createElement("table", {
      key: sms_gr_id
    }, /*#__PURE__*/React.createElement("tbody", null, /*#__PURE__*/React.createElement("tr", {
      key: "t"
    }, /*#__PURE__*/React.createElement("th", null, "data"), /*#__PURE__*/React.createElement("th", null, "godzina"), /*#__PURE__*/React.createElement("th", null, "uczestnik"), /*#__PURE__*/React.createElement("th", null, "tresc")), sms_rows));
  });
  return /*#__PURE__*/React.createElement("div", {
    className: "send_messages_dialog"
  }, grupy);
}
modular.program.panels.smsWew = PanelSMSWew;

/*
setTimeout(() => {
    modular.program.showPanel("smsWew")
}, 1000)*/
//# sourceMappingURL=panel_sms_wew.js.map