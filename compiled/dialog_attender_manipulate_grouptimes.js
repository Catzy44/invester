import { Dialog, showDialog } from "./dialog.js";
import { miesiacF, PlaceType } from "./panel_platnosci.js";
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
    payments: null,
    season: null,
    closedMonths: null,
    place: null,
    biggestCycleNum: null
  };
  constructor(props) {
    super(props);
    this.attenderId = props.attenderId;
    const errorF = err => {
      console.log(err);
      injectRField(this, "error")(err);
    };
    gather(`uczestnicy/${this.attenderId}/payments/fresh`, injectRField(this, "payments"), errorF);
    gather(`uczestnicy/${this.attenderId}/season`, injectRField(this, "season"), errorF);
    gather(`uczestnicy/${this.attenderId}/closedMonths`, injectRField(this, "closedMonths"), errorF);
    gather(`uczestnicy/${this.attenderId}`, attenderData => {
      injectRField(this, "attender")(attenderData);
      gather(`uczestnicy/${this.attenderId}/miejsce`, placeData => {
        injectRField(this, "place")(placeData);
        const rodzaj = placeData.rodzaj;
        if (rodzaj == PlaceType.TYPE_ADULTS) {
          gather(`grupy/${attenderData.groupId}/biggestCycleNum`, injectRField(this, "biggestCycleNum"), errorF);
        }
      }, errorF);
    }, errorF);
    //gather(`uczestnicy/${this.attenderId}/at`,this,"place",errorF)
  }

  dialog = createRef();
  render() {
    if (this.state.payments == null) return null;
    if (this.state.season == null) return null;
    if (this.state.closedMonths == null) return null;
    if (this.state.place == null) {
      return null;
    }
    const monthsRows = [];
    const attenderId = this.attenderId;
    const placeType = this.state.place.rodzaj;
    if (placeType == PlaceType.TYPE_ADULTS) {
      if (this.state.biggestCycleNum == null) {
        return null;
      }
      const biggestCycleNum = this.state.biggestCycleNum;
      for (let i = 1; i < biggestCycleNum + 1; i++) {
        const {
          status,
          amountPaid,
          amountToPay,
          closed
        } = this.state.payments[i];
        const cm = this.state.closedMonths.find(({
          id,
          season,
          month,
          year,
          cycle,
          amount
        }) => cycle === i);
        function AlterPaymentCell(props) {
          const [cm, setCM] = useState(props.closedMonth);
          const [locked, setLocked] = useState(cm != null);
          const [amount, setAmount] = useState(locked && cm != null ? cm.amount : props.freshAmount);
          const [altering, setAlteringMode] = useState(false);
          const [processing, setProcessing] = useState(false);
          const inp = createRef();
          if (altering) {
            return /*#__PURE__*/React.createElement("div", {
              className: `alter_payment_el${processing ? " processing" : ""}`
            }, /*#__PURE__*/React.createElement("input", {
              type: "number",
              defaultValue: amount,
              ref: inp
            }), /*#__PURE__*/React.createElement("i", {
              className: "fa-solid fa-floppy-disk",
              onClick: () => {
                if (inp.current == null) {
                  return;
                }
                const inputValue = inp.current.value;
                setProcessing(true);
                fet("closedMonths", {
                  method: "POST",
                  body: JSON.stringify({
                    attenderId: attenderId,
                    cycle: props.cycleNum,
                    amount: inputValue
                  })
                }).then(res => {
                  setProcessing(false);
                  setCM(res);
                  setAmount(inputValue);
                  setLocked(true);
                  setAlteringMode(false);
                }).catch(err => {
                  setProcessing(false);
                  console.log(err);
                });
              }
            }), /*#__PURE__*/React.createElement("i", {
              className: "fa-solid fa-trash",
              onClick: () => {
                if (cm == null) {
                  setAlteringMode(false);
                  return;
                }
                setProcessing(true);
                fet(`closedMonths/${cm.id}`, {
                  method: "DELETE"
                }).then(res => {
                  setProcessing(false);
                  setCM(null);
                  setAmount(props.freshAmount);
                  setLocked(false);
                  setAlteringMode(false);
                }).catch(err => {
                  setProcessing(false);
                  console.log(err);
                });
              }
            }), /*#__PURE__*/React.createElement("i", {
              className: "fa-solid fa-x",
              onClick: () => {
                setAlteringMode(false);
              }
            }));
          }
          function quickcopy() {
            setProcessing(true);
            fet("closedMonths", {
              method: "POST",
              body: JSON.stringify({
                attenderId: attenderId,
                cycle: props.cycleNum,
                amount: props.freshAmount
              })
            }).then(res => {
              setProcessing(false);
              setCM(res);
              setAmount(props.freshAmount);
              setLocked(true);
              setAlteringMode(false);
            }).catch(err => {
              setProcessing(false);
              console.log(err);
            });
          }
          return locked ? /*#__PURE__*/React.createElement("div", {
            className: `alter_payment_el${processing ? " processing" : ""}`
          }, /*#__PURE__*/React.createElement("span", null, "ustalona: ", amount), /*#__PURE__*/React.createElement("input", {
            type: "button",
            value: "zmie\u0144",
            onClick: () => {
              setAlteringMode(true);
            }
          }), /*#__PURE__*/React.createElement("input", {
            className: "quickcopy",
            type: "button",
            value: "<",
            onClick: quickcopy
          })) : /*#__PURE__*/React.createElement("div", {
            className: `alter_payment_el${processing ? " processing" : ""}`
          }, /*#__PURE__*/React.createElement("span", null, "nieustalona"), /*#__PURE__*/React.createElement("input", {
            type: "button",
            value: "ustal",
            onClick: () => {
              setAlteringMode(true);
            }
          }), /*#__PURE__*/React.createElement("input", {
            className: "quickcopy",
            type: "button",
            value: "<",
            onClick: quickcopy
          }));
        }
        monthsRows.push( /*#__PURE__*/React.createElement("tr", {
          key: i
        }, /*#__PURE__*/React.createElement("td", null, "Cykl ", i), /*#__PURE__*/React.createElement("td", {
          className: "cm_cell"
        }, /*#__PURE__*/React.createElement(AlterPaymentCell, {
          cycleNum: i,
          closedMonth: cm,
          freshAmount: amountToPay
        })), /*#__PURE__*/React.createElement("td", null, amountToPay)));
      }
    } else {
      const startingMonth = parseInt(this.state.season['odStr'].split('-')[1]) - 1;
      const fromFirstToLatestMonth = funct => {
        for (let i = 0; i < 12; i++) {
          const monthIdx = (startingMonth + i) % 12;
          funct(monthIdx);
        }
      };
      fromFirstToLatestMonth(monthIdx => {
        const {
          status,
          amountPaid,
          amountToPay,
          closed
        } = this.state.payments[monthIdx];
        const cm = this.state.closedMonths.find(({
          id,
          season,
          month,
          year,
          cycle,
          amount
        }) => month === monthIdx);
        function AlterPaymentCell(props) {
          const [cm, setCM] = useState(props.closedMonth);
          const [locked, setLocked] = useState(cm != null);
          const [amount, setAmount] = useState(locked && cm != null ? cm.amount : props.freshAmount);
          const [altering, setAlteringMode] = useState(false);
          const [processing, setProcessing] = useState(false);
          const inp = createRef();
          if (altering) {
            return /*#__PURE__*/React.createElement("div", {
              className: `alter_payment_el${processing ? " processing" : ""}`
            }, /*#__PURE__*/React.createElement("input", {
              type: "number",
              defaultValue: amount,
              ref: inp
            }), /*#__PURE__*/React.createElement("i", {
              className: "fa-solid fa-floppy-disk",
              onClick: () => {
                if (inp.current == null) {
                  return;
                }
                const inputValue = inp.current.value;
                setProcessing(true);
                fet("closedMonths", {
                  method: "POST",
                  body: JSON.stringify({
                    attenderId: attenderId,
                    month: props.monthNum,
                    amount: inputValue
                  })
                }).then(res => {
                  setProcessing(false);
                  setCM(res);
                  setAmount(inputValue);
                  setLocked(true);
                  setAlteringMode(false);
                }).catch(err => {
                  setProcessing(false);
                  console.log(err);
                });
              }
            }), /*#__PURE__*/React.createElement("i", {
              className: "fa-solid fa-trash",
              onClick: () => {
                if (cm == null) {
                  setAlteringMode(false);
                  return;
                }
                setProcessing(true);
                fet(`closedMonths/${cm.id}`, {
                  method: "DELETE"
                }).then(res => {
                  setProcessing(false);
                  setCM(null);
                  setAmount(props.freshAmount);
                  setLocked(false);
                  setAlteringMode(false);
                }).catch(err => {
                  setProcessing(false);
                  console.log(err);
                });
              }
            }), /*#__PURE__*/React.createElement("i", {
              className: "fa-solid fa-x",
              onClick: () => {
                setAlteringMode(false);
              }
            }));
          }
          function quickcopy() {
            setProcessing(true);
            fet("closedMonths", {
              method: "POST",
              body: JSON.stringify({
                attenderId: attenderId,
                month: props.monthNum,
                amount: props.freshAmount
              })
            }).then(res => {
              setProcessing(false);
              setCM(res);
              setAmount(props.freshAmount);
              setLocked(true);
              setAlteringMode(false);
            }).catch(err => {
              setProcessing(false);
              console.log(err);
            });
          }
          return locked ? /*#__PURE__*/React.createElement("div", {
            className: `alter_payment_el${processing ? " processing" : ""}`
          }, /*#__PURE__*/React.createElement("span", null, "ustalona: ", amount), /*#__PURE__*/React.createElement("input", {
            type: "button",
            value: "zmie\u0144",
            onClick: () => {
              setAlteringMode(true);
            }
          }), /*#__PURE__*/React.createElement("input", {
            className: "quickcopy",
            type: "button",
            value: "<",
            onClick: quickcopy
          })) : /*#__PURE__*/React.createElement("div", {
            className: `alter_payment_el${processing ? " processing" : ""}`
          }, /*#__PURE__*/React.createElement("span", null, "nieustalona"), /*#__PURE__*/React.createElement("input", {
            type: "button",
            value: "ustal",
            onClick: () => {
              setAlteringMode(true);
            }
          }), /*#__PURE__*/React.createElement("input", {
            className: "quickcopy",
            type: "button",
            value: "<",
            onClick: quickcopy
          }));
        }
        monthsRows.push( /*#__PURE__*/React.createElement("tr", {
          key: monthIdx
        }, /*#__PURE__*/React.createElement("td", null, miesiacF(monthIdx)), /*#__PURE__*/React.createElement("td", {
          className: "cm_cell"
        }, /*#__PURE__*/React.createElement(AlterPaymentCell, {
          monthNum: monthIdx,
          closedMonth: cm,
          freshAmount: amountToPay
        })), /*#__PURE__*/React.createElement("td", null, amountToPay)));
      });
    }
    return /*#__PURE__*/React.createElement(Dialog, {
      ref: this.dialog,
      title: "rozliczenia uczestnika"
    }, /*#__PURE__*/React.createElement("div", {
      className: "dialog_attender_manipulate_payments"
    }, /*#__PURE__*/React.createElement("table", null, /*#__PURE__*/React.createElement("tbody", null, /*#__PURE__*/React.createElement("tr", {
      key: "tit"
    }, /*#__PURE__*/React.createElement("th", null, placeType == PlaceType.TYPE_ADULTS ? "Cykl" : "Miesiąc"), /*#__PURE__*/React.createElement("th", null, "kwota za ten miesi\u0105c"), /*#__PURE__*/React.createElement("th", null, "wyliczone")), monthsRows)), /*#__PURE__*/React.createElement("input", {
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
//# sourceMappingURL=dialog_attender_manipulate_grouptimes.js.map