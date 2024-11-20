import { Dialog, showDialog } from "./dialog.js";
import { miesiacF, PlaceType } from "./panel_platnosci.js";
import { ClassGroupSelectorComp } from "./components_common.js";
const {
  useEffect,
  useState,
  createRef,
  useCallback,
  cloneElement
} = React;
export class DialogAttenderManipulateGroupChanges extends React.Component {
  attenderId = -1;
  state = {
    groupChanges: null,
    processing: false
  };
  constructor(props) {
    super(props);
    this.attenderId = props.attenderId;
    const errorF = err => {
      console.log(err);
      injectRField(this, "error")(err);
    };
    gather(`uczestnicy/${this.attenderId}/groupChanges`, injectRField(this, "groupChanges"), errorF);
    gather(`uczestnicy/${this.attenderId}/season`, injectRField(this, "season"), errorF);
    gather(`uczestnicy/${this.attenderId}`, attenderData => {
      injectRField(this, "attender")(attenderData);
      gather(`uczestnicy/${this.attenderId}/place`, injectRField(this, "place"), errorF);
    }, errorF);

    //gather(`uczestnicy/${this.attenderId}/at`,this,"place",errorF)
  }

  render() {
    if (this.state.groupChanges == null) return null;
    if (this.state.place == null) return null;
    if (this.state.season == null) return null;
    const attenderId = this.attenderId;
    const groupChangesRows = this.state.groupChanges.map(change => {
      const {
        id,
        idRoku,
        group,
        active,
        dateJoined,
        dateLeft
      } = change;
      const {
        name
      } = group ?? {};
      function AlterDateCell(props) {
        const [date, setDate] = useState(props.date);
        const [editing, setEditing] = useState(false);
        const [processing, setProcessing] = useState(false);
        const [locked, setLocked] = useState(false);
        const [inpDate, setInpDate] = useState(date);
        const mode = props.mode;
        if (editing) {
          return /*#__PURE__*/React.createElement("div", {
            className: `alter_date_el editing${processing ? " processing" : ""}`
          }, /*#__PURE__*/React.createElement("input", {
            type: "date",
            value: inpDate,
            onChange: e => setInpDate(e.target.value)
          }), /*#__PURE__*/React.createElement("i", {
            className: "fa-solid fa-floppy-disk",
            onClick: () => {
              setProcessing(true);
              setLocked(true);
              const obj = {
                id
              };
              if (mode == "joined") {
                obj["dateJoined"] = inpDate;
              } else {
                obj["dateLeft"] = inpDate;
              }
              fet("groupChanges", {
                method: "PATCH",
                body: JSON.stringify(obj)
              }).then(res => {
                setProcessing(false);
                setDate(inpDate);
                setLocked(false);
                setEditing(false);
              }).catch(err => {
                setProcessing(false);
                setLocked(false);
                console.log(err);
              });
            }
          }), /*#__PURE__*/React.createElement("i", {
            className: "fa-solid fa-x",
            onClick: () => {
              setInpDate(date);
              setEditing(false);
            }
          }));
        }
        function formatDateStr(str) {
          const date = str.split("-");
          return date[2] + " . " + date[1] + " . " + date[0];
        }
        return /*#__PURE__*/React.createElement("div", {
          className: "alter_date_el"
        }, /*#__PURE__*/React.createElement("span", null, formatDateStr(date)), /*#__PURE__*/React.createElement("i", {
          className: "fa-solid fa-pen",
          onClick: () => {
            setEditing(true);
          }
        }));
      }
      return /*#__PURE__*/React.createElement("tr", {
        key: id,
        className: `${this.state.processing ? " processing" : ""}`
      }, /*#__PURE__*/React.createElement("td", null, name == null ? /*#__PURE__*/React.createElement("span", {
        className: "red"
      }, "wypisany") : /*#__PURE__*/React.createElement("span", null, name)), /*#__PURE__*/React.createElement("td", {
        className: "cm_cell"
      }, /*#__PURE__*/React.createElement(AlterDateCell, {
        date: dateJoined,
        id: id,
        mode: "joined"
      })), /*#__PURE__*/React.createElement("td", {
        className: "cm_cell"
      }, /*#__PURE__*/React.createElement(AlterDateCell, {
        date: dateLeft,
        id: id,
        mode: "left"
      })), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("i", {
        className: "fa-solid fa-trash grow",
        onClick: () => {
          this.setState(state => ({
            ...state,
            processing: true
          }));
          fet(`groupChanges/${id}`, {
            method: "DELETE"
          }).then(res => {
            this.setState(state => ({
              ...state,
              groupChanges: state.groupChanges.filter(ch => ch.id != id),
              processing: false
            }));
          });
        }
      }))));
    });
    return /*#__PURE__*/React.createElement(Dialog, {
      title: "przepisy uczestnika"
    }, /*#__PURE__*/React.createElement("div", {
      className: "dialog_attender_manipulate_groupchanges"
    }, /*#__PURE__*/React.createElement("table", null, /*#__PURE__*/React.createElement("tbody", null, /*#__PURE__*/React.createElement("tr", {
      key: "tit"
    }, /*#__PURE__*/React.createElement("th", null, "grupa"), /*#__PURE__*/React.createElement("th", null, "data do\u0142\u0105czenia"), /*#__PURE__*/React.createElement("th", null, "data opuszczenia"), /*#__PURE__*/React.createElement("th", null, "akcja")), groupChangesRows, /*#__PURE__*/React.createElement(AddNewGroupchangeRow, {
      place: this.state.place,
      season: this.state.season,
      attenderId: attenderId,
      parent: this
    }))), /*#__PURE__*/React.createElement("input", {
      onClick: showHelp,
      value: "pomoc",
      type: "button"
    })));
  }
}
function AddNewGroupchangeRow(props) {
  const [groupId, setGroupId] = useState(null);
  const placeId = props.place.id;
  const attenderId = props.attenderId;
  const {
    id,
    nazwa,
    timestamp,
    startDate,
    endDate
  } = props.season;
  const [dateFrom, setDateFrom] = useState(startDate);
  const [dateTo, setDateTo] = useState(endDate);
  const [processing, setProcessing] = useState(false);
  const isGroupSelected = () => {
    return groupId != null && groupId != 0;
  };
  return /*#__PURE__*/React.createElement("tr", {
    className: `newentry${processing ? " processing" : ""}`
  }, /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement(ClassGroupSelectorComp, {
    placeId: placeId,
    onChange: val => setGroupId(val)
  })), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("input", {
    type: "date",
    onChange: ev => setDateFrom(ev.target.value),
    defaultValue: startDate
  })), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("input", {
    type: "date",
    onChange: ev => setDateTo(ev.target.value),
    defaultValue: endDate
  })), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("i", {
    className: `fa-solid fa-plus grow${!isGroupSelected() ? " incomplete" : ""}`,
    onClick: () => {
      if (!isGroupSelected()) {
        return;
      }
      setProcessing(true);
      fet("groupChanges", {
        method: "POST",
        body: {
          dateJoined: dateFrom,
          dateLeft: dateTo,
          groupId: groupId,
          attenderId: attenderId
        }
      }).then(res => {
        setProcessing(false);
        props.parent.setState(state => {
          state.groupChanges.push(res);
          return {
            ...state,
            groupChanges: state.groupChanges
          };
        });
      });
    }
  }))));
}
function showHelp() {
  function HelpDialog(props) {
    return /*#__PURE__*/React.createElement(Dialog, null, /*#__PURE__*/React.createElement("div", {
      title: "pomoc"
    }, /*#__PURE__*/React.createElement("p", null, "narz\u0119dzie s\u0142u\u017Cy do ustalania w jakiej grupie by\u0142 dany uczestnik w danym okresie czasu"), /*#__PURE__*/React.createElement("p", null, "ka\u017Cdy wiersz to okres czasu jaki uczestnik sp\u0119dzi\u0142 w jakiej\u015B grupie tanecznej, dalej \"okres\""), /*#__PURE__*/React.createElement("p", null, "system zlicza zaj\u0119cia grup z danych okres\xF3w i na podstawie tego oblicza nale\u017Cno\u015Bci"), /*#__PURE__*/React.createElement("p", null, "zakresy dat okres\xF3w NIE MOG\u0104 si\u0119 na siebie nak\u0142ada\u0107"), /*#__PURE__*/React.createElement("p", null, "zakresy dat okres\xF3w mog\u0105 mie\u0107 pomi\u0119dzy sob\u0105 \"dziury\", dla systemu oznacza to tyle \u017Ce uczestnik w tamtym okresie czasu (w czasie dziury) nie ucz\u0119szcza\u0142 na \u017Cadne zaj\u0119cia - by\u0142 wypisany"), /*#__PURE__*/React.createElement("p", null, "pasek dodawania nowego okresu na dole ma z automatu wpisan\u0105 dat\u0119 rozpocz\u0119cia i zako\u0144czenia aktualnego sezonu, dzi\u0119ki czemu mo\u017Cna szybko doda\u0107 zakres na ca\u0142y sezon (rok zaj\u0119ciowy)"), /*#__PURE__*/React.createElement("p", null, "WA\u017BNE: nie zapomnij doda\u0107 okresu dla grupy w kt\xF3rej aktualnie jest uczestnik!!!")));
  }
  showDialog( /*#__PURE__*/React.createElement(HelpDialog, null));
}

/*setTimeout(()=>{
    showDialog(<DialogAttenderManupulatePayments attenderId={9821}/>)
},1000)*/
//# sourceMappingURL=dialog_attender_manipulate_groupchanges.js.map