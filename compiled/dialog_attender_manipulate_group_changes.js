import { Dialog, showDialog } from "./dialog.js";
import { miesiacF, PlaceType } from "./panel_platnosci.js";
import { gather, zebra } from "./main.js";
import { ClassGroupSelectorComp } from "../compiled/components_common";
import { jsx as ___EmotionJSX } from "@emotion/react";
const {
  useEffect,
  useState,
  createRef,
  useCallback,
  cloneElement
} = React;
export class DialogAttenderManupulateGroupChanges extends React.Component {
  attenderId = -1;
  state = {
    payments: null,
    season: null,
    groupChanges: null,
    place: null,
    biggestCycleNum: null
  };
  constructor(props) {
    super(props);
    this.attenderId = props.attenderId;
    const errorF = err => {
      console.log(err);
      this.setState({
        ...this.state,
        error: err
      });
    };
    gather(`uczestnicy/${this.attenderId}/payments/fresh`, this, "payments", errorF);
    gather(`uczestnicy/${this.attenderId}/season`, this, "season", errorF);
    gather(`uczestnicy/${this.attenderId}/groupChanges`, this, "groupChanges", errorF);
    gather(`uczestnicy/${this.attenderId}`, this, "attender", errorF, attender => {
      gather(`uczestnicy/${this.attenderId}/place`, this, "place", errorF, miejsce => {
        const rodzaj = miejsce.rodzaj;
        if (rodzaj == PlaceType.TYPE_ADULTS) {
          gather(`grupy/${attender.groupId}/biggestCycleNum`, this, "biggestCycleNum", errorF);
        }
      });
    });
    //gather(`uczestnicy/${this.attenderId}/at`,this,"place",errorF)
  }
  dialog = createRef();
  render() {
    if (this.state.payments == null) return null;
    if (this.state.season == null) return null;
    if (this.state.groupChanges == null) return null;
    if (this.state.place == null) {
      return null;
    }
    function formatDate(date) {
      const datespl = date.split("-");
      return datespl[2] + " - " + datespl[1] + " - " + datespl[0];
    }
    const dialog = this;
    const groupchangesRows = this.state.groupChanges.map(gc => {
      const {
        id,
        idRoku,
        group,
        active,
        dateJoined,
        dateLeft
      } = gc;
      function DateCell(props) {
        let date = props.opt === "joined" ? dateJoined : dateLeft;
        const [isEditing, setIsEditing] = useState(false);
        const [enteredDate, setEnteredDate] = useState(date);
        const [processing, setProcessing] = useState(false);
        if (isEditing) {
          return /*#__PURE__*/React.createElement("div", {
            className: `alter_date_el${processing ? " processing" : ""}`
          }, /*#__PURE__*/React.createElement("input", {
            type: "date",
            value: enteredDate,
            onChange: ev => {
              setEnteredDate(ev.target.value);
            }
          }), /*#__PURE__*/React.createElement("i", {
            className: "fa-regular fa-floppy-disk",
            onClick: () => {
              let body = null;
              if (props.opt === "joined") {
                body = {
                  id: id,
                  dateJoined: enteredDate
                };
              } else {
                body = {
                  id: id,
                  dateLeft: enteredDate
                };
              }
              setProcessing(true);
              zebra("groupChanges", res => {
                setProcessing(false);
                dialog.setState(state => {
                  state.groupChanges = state.groupChanges.map(groupChange => {
                    if (groupChange.id !== id) {
                      return groupChange;
                    }
                    return res;
                  });
                  return state;
                });
              }, null, {
                method: "PATCH",
                body: body
              });
              setIsEditing(false);
            }
          }), /*#__PURE__*/React.createElement("i", {
            className: "fa-solid fa-x",
            onClick: () => {
              setEnteredDate(date);
              setIsEditing(false);
            }
          }));
        }
        return /*#__PURE__*/React.createElement("div", {
          className: `alter_date_el${processing ? " processing" : ""}`
        }, /*#__PURE__*/React.createElement("span", null, formatDate(date)), /*#__PURE__*/React.createElement("i", {
          className: "fa-solid fa-pen-to-square",
          onClick: () => {
            setIsEditing(true);
          }
        }));
      }
      return /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null, group.name), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement(DateCell, {
        opt: "joined"
      })), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement(DateCell, {
        opt: "left"
      })), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("i", {
        className: "fa-solid fa-trash"
      }))));
    });
    function AddNewGroupChangeRow(props) {
      const {
        selectedGroup,
        setSelectedGroup
      } = useState(0);
      const {
        dateFrom,
        setDateFrom
      } = useState(season.startDate);
      const {
        dateTo,
        setDateTo
      } = useState(season.endDate);
      const groupSelectorChanged = res => {
        //console.log(res)
        //console.log(selectedGroup)
        //debugger
        setSelectedGroup(res);
      };
      return /*#__PURE__*/React.createElement("tr", {
        key: "addnew"
      }, /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement(ClassGroupSelectorComp, {
        placeId: dialog.state.place.id,
        onChange: groupSelectorChanged
      })), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("input", {
        type: "date",
        value: dateFrom,
        onChange: ev => {
          setDateFrom(ev.target.value);
        }
      })), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("input", {
        type: "date",
        value: dateTo,
        onChange: ev => {
          setDateTo(ev.target.value);
        }
      })), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("i", {
        className: "fa-solid fa-plus",
        onClick: () => {
          zebra("groupChanges", res => {
            dialog.setState(state => {
              state.groupChanges.push(res);
              return state;
            });
          }, null, {
            method: "POST",
            body: {
              groupId: selectedGroup,
              attenderId: dialog.attenderId,
              dateJoined: dateFrom,
              dateLeft: dateTo
            }
          });
        }
      }))));
    }
    const season = this.state.season;
    return /*#__PURE__*/React.createElement(Dialog, {
      ref: this.dialog,
      title: "przepisy uczestnika"
    }, /*#__PURE__*/React.createElement("div", {
      className: "dialog_attender_manipulate_groupchanges"
    }, /*#__PURE__*/React.createElement("table", null, /*#__PURE__*/React.createElement("tbody", null, /*#__PURE__*/React.createElement("tr", {
      key: "tit"
    }, /*#__PURE__*/React.createElement("th", null, "grupa"), /*#__PURE__*/React.createElement("th", null, "data do\u0142\u0105czenia"), /*#__PURE__*/React.createElement("th", null, "data opuszczenia"), /*#__PURE__*/React.createElement("th", null)), groupchangesRows, /*#__PURE__*/React.createElement(AddNewGroupChangeRow, null))), /*#__PURE__*/React.createElement("input", {
      onClick: showHelp,
      value: "pomoc",
      type: "button"
    })));
  }
}
function showHelp() {
  function HelpDialog(props) {
    return /*#__PURE__*/React.createElement(Dialog, null, /*#__PURE__*/React.createElement("div", {
      title: "pomoc"
    }, /*#__PURE__*/React.createElement("p", null, "ustalenie kwoty za dany miesi\u0105c sprawia \u017Ce staje si\u0119 ona \"statyczna\" - \u017Cadna zmiana typu zmiana grupy zaj, zmiana rabatu, etc nie wp\u0142ynie na ni\u0105"), /*#__PURE__*/React.createElement("p", null, "usuni\u0119cie \"ustalenia kwoty\" spowoduje \u017Ce kwota w danym miesi\u0105cu stanie si\u0119 \"dynamiczna\" - znowu b\u0119d\u0105 na ni\u0105 wp\u0142ywa\u0107 wszystkie zmiany"), /*#__PURE__*/React.createElement("p", null, "kwota w kolumnie \"wyliczone\" jest kwot\u0105 wynikaj\u0105c\u0105 z \"aktualnego stanu rzeczy\" - jest to kwota policzona wed\u0142ug dost\u0119pnych na ten moment danych"), /*#__PURE__*/React.createElement("p", null, "u\u017Cycie strza\u0142ki spowoduje ustalenie kwoty za dany miesi\u0105c na kwot\u0119 z kolumny \"wyliczone\""), /*#__PURE__*/React.createElement("p", null, "program przy najbli\u017Cszej mo\u017Cliwej okazji automatycznie zamknie wszystkie miesi\u0105ce dla kt\xF3rych mo\u017Ce zosta\u0107 wykonana weryfikacja p\u0142atno\u015Bci"), /*#__PURE__*/React.createElement("p", null, "program NIE zamyka automatycznie miesi\u0119cy w kt\xF3rych nie by\u0142o \u017Cadnych zaj\u0119\u0107"), /*#__PURE__*/React.createElement("p", null, "program NIE zamyka automatycznie miesi\u0119cy je\u017Celi w dowolnym poprzedzaj\u0105cym miesi\u0105cu wyst\u0119puje zaleg\u0142o\u015B\u0107")));
  }
  showDialog( /*#__PURE__*/React.createElement(HelpDialog, null));
}

/*setTimeout(()=>{
    showDialog(<DialogAttenderManupulatePayments attenderId={9821}/>)
},1000)*/
//# sourceMappingURL=dialog_attender_manipulate_group_changes.js.map