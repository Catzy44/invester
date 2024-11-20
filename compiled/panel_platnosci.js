/*modular.panels.payments = {
    show:(callback)=>{
        const platnosciPanelRoot = ReactDOM.createRoot(document.getElementById("ppanel"))
        platnosciPanelRoot.render(<PanelPlatnosci miejsceId={modular.selectors.place}/>)
        callback()
    },
    name: "payments"
}*/

import { showDialog } from "./dialog.js";
import { DialogAttenderManupulatePayments } from "./dialog_attender_manipulate_payments.js";
import { DialogAttenderManupulateGroupChanges } from "./dialog_attender_manipulate_group_changes";
import { DialogInstructorInfo } from "./dialog_instructor_info";
import { DialogPlaceInfo } from "./dialog_place_info";
import { jsx as ___EmotionJSX } from "@emotion/react";
export class GroupAttendersInfoTableRows extends React.Component {
  state = {
    groupInfo: null,
    error: null,
    attenderArr: null
  };
  constructor(props) {
    super(props);
    this.id = props.groupId;
    const errorF = err => {
      console.log(err);
      this.setState({
        ...this.state,
        error: err
      });
    };
    if (this.id == 0) {
      //discharged
      fet(`miejsca/${this.props.placeId}/attendersDischarged`).then(ret => {
        this.setState({
          ...this.state,
          attenderArr: ret,
          groupInfo: {
            name: "Wypisani",
            classesStartTime: "--",
            classesEndTime: "--"
          }
        });
      }).catch(errorF);
      return;
    } else if (this.id == -1) {
      //discharged temp
      fet(`miejsca/${this.props.placeId}/attendersReserve`).then(ret => {
        this.setState({
          ...this.state,
          attenderArr: ret,
          groupInfo: {
            name: "Rezerwowa",
            classesStartTime: "--",
            classesEndTime: "--"
          }
        });
      }).catch(errorF);
      return;
    }
    fet(`grupy/${this.id}`).then(ret => {
      this.setState({
        ...this.state,
        groupInfo: ret
      });
      fet(`grupy/${this.id}/attenders`).then(ret => {
        this.setState({
          ...this.state,
          attenderArr: ret
        });
      }).catch(errorF);
    }).catch(errorF);
  }
  render() {
    if (this.state.error != null) {
      return /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null, "Grupa ", this.id, " B\u0142\u0105d! ", this.state.error));
    }
    if (this.state.groupInfo == null) {
      return /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null));
      //return <tr><td>Wczytywanie danych grupy {this.id}...</td></tr>
    }
    if (this.state.attenderArr == null) {
      return /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null));
      //return <tr><td>Wczytywanie uczestników grupy {this.id}...</td></tr>
    }
    const groupInfo = this.state.groupInfo;
    const {
      timestamp,
      idRoku,
      name: groupName,
      classesStartTime,
      classesEndTime,
      dzienZajec,
      cenaZaJedneZajecia,
      uczestnicy
    } = groupInfo;
    const GrSelectionQuad = props => {
      const isSelected = props.panel.state.selectedGroup === props.groupId || props.panel.state.selectedGroups != null && props.panel.state.selectedGroups.includes(props.attenderId);
      return /*#__PURE__*/React.createElement("th", {
        className: "selection-box",
        onClick: () => {
          if (props.panel.state.selectedAttenders != null) {
            if (props.panel.state.selectedGroups.includes(props.groupId)) {
              this.state.attenderArr.forEach(att => {
                props.panel.setSelectedAttender(att.id, false);
              });
              props.panel.setSelectedGroup(props.groupId, false);
            } else {
              this.state.attenderArr.forEach(att => {
                props.panel.setSelectedAttender(att.id, true);
              });
              props.panel.setSelectedGroup(props.groupId, true);
            }
            return;
          }
          props.panel.setSelectedGroup(props.groupId);
        }
      }, isSelected ? "v" : "");
    };
    const AttSelectionQuad = props => {
      const isSelectedAttender = props.panel.state.selectedAttender === props.attenderId || props.panel.state.selectedAttenders != null && props.panel.state.selectedAttenders.includes(props.attenderId);
      return /*#__PURE__*/React.createElement("th", {
        className: "selection-box",
        onClick: () => {
          if (props.panel.state.selectedAttenders != null) {
            props.panel.setSelectedAttender(props.attenderId, !isSelectedAttender);
            return;
          }
          props.panel.setSelectedAttender(props.attenderId);
        }
      }, isSelectedAttender ? "v" : "");
    };
    const attenderArr = this.state.attenderArr;
    const attenders_rows = attenderArr.map(({
      id,
      /*imie,nazwisko,*/grupaPrzedszkolna,
      /*telefonOpiekuna,*/umowa,
      kolor,
      interakcja,
      attenderInfo,
      parentInfo
    }, idx) => {
      const isRowSelected = this.props.panel.state.selectedAttender == id;
      let style = {};
      const midCol = kolor != null ? kolor : "white";
      const leftCol = interakcja == 1 ? "orange" : midCol;
      const rightCol = umowa == 0 ? "red" : midCol;
      style.backgroundImage = `linear-gradient(to right, ${leftCol} 8%, ${midCol} 10%, ${midCol} 90%,${rightCol} 92%)`;
      return /*#__PURE__*/React.createElement("tr", {
        style: style,
        key: "payments_attenderinfo_c" + this.id + "_" + idx,
        className: `userinfo${isRowSelected ? " row-selected" : ""}`
      }, /*#__PURE__*/React.createElement("td", null, idx + 1), /*#__PURE__*/React.createElement(AttSelectionQuad, {
        panel: this.props.panel,
        attenderId: id
      }), /*#__PURE__*/React.createElement("td", null, `${attenderInfo.firstName} ${attenderInfo.lastName}`), /*#__PURE__*/React.createElement("td", null, grupaPrzedszkolna), /*#__PURE__*/React.createElement("td", null, beautifyPhoneNum(parentInfo.number)), /*#__PURE__*/React.createElement("td", null, umowaf(umowa)));
    });
    return /*#__PURE__*/React.createElement(React.Fragment, null, /*#__PURE__*/React.createElement("tr", {
      key: "payments_attenderinfo_a" + this.id,
      className: "label"
    }, /*#__PURE__*/React.createElement("th", null), /*#__PURE__*/React.createElement("th", null), this.id == 0 || this.id == 1 ? /*#__PURE__*/React.createElement("th", {
      colSpan: "3"
    }, `${groupName}`) : /*#__PURE__*/React.createElement("th", {
      colSpan: "3"
    }, `${groupName} [${classesStartTime} - ${classesEndTime}]`), /*#__PURE__*/React.createElement("th", null)), /*#__PURE__*/React.createElement("tr", {
      key: "payments_attenderinfo_b" + this.id,
      className: "label"
    }, /*#__PURE__*/React.createElement("th", null, "lp"), /*#__PURE__*/React.createElement(GrSelectionQuad, {
      panel: this.props.panel,
      groupId: this.id
    }), /*#__PURE__*/React.createElement("th", null, "imi\u0119 i nazwisko"), /*#__PURE__*/React.createElement("th", null, "grupa przedszkolna"), /*#__PURE__*/React.createElement("th", null, "nr tel"), /*#__PURE__*/React.createElement("th", null, "u")), attenders_rows);
  }
}
class GroupAttendersPaymentsTableRows extends React.Component {
  state = {
    groupInfo: null,
    error: null,
    attenderArr: null,
    classPricesForAllMonths: null,
    usersPaymentInfoArr: null,
    seasonInfo: null
  };
  constructor(props) {
    super(props);
    this.id = props.groupId;
    const errorF = err => {
      console.log(err);
      this.setState({
        ...this.state,
        error: err
      });
    };
    if (this.id == 0 || this.id == 0) {
      /* SPECIAL CASE: DISCHARGED ATTENDERS */
      fet(`miejsca/${this.props.placeId}/attendersDischarged`).then(attarr => {
        this.setState({
          ...this.state,
          attenderArr: attarr,
          groupInfo: {
            nazwa: "Wypisani"
          }
        });
        const fetchNextUser = (index = 0) => {
          const attender = attarr[index];
          if (attender == null) {
            return;
          }
          fet(`uczestnicy/${attender['id']}/payments`).then(ret => {
            this.setState(state => {
              state.attenderArr[index]['payments'] = ret;
              return state;
            });
            fetchNextUser(index + 1);
          }).catch(errorF);
        };
        fetchNextUser();
      }).catch(errorF);
      fet(`miejsca/${this.props.placeId}/season`).then(ret => {
        this.setState(state => {
          return {
            ...state,
            seasonInfo: ret
          };
        });
      }).catch(errorF);
    } else {
      /* DEFAULT CASE */
      fet(`grupy/${this.id}`).then(ret => {
        this.setState(state => {
          return {
            ...state,
            groupInfo: ret
          };
        });
        setTimeout(() => {
          fet(`grupy/${this.id}/attenders`).then(attarr => {
            this.setState({
              ...this.state,
              attenderArr: attarr
            });
            const fetchNextUser = (index = 0) => {
              const attender = attarr[index];
              if (attender == null) {
                return;
              }
              fet(`uczestnicy/${attender['id']}/payments`).then(ret => {
                this.setState(state => {
                  state.attenderArr[index]['payments'] = ret;
                  return state;
                });
                fetchNextUser(index + 1);
              }).catch(errorF);
            };
            fetchNextUser();
          }).catch(errorF);
        }, 100);
      }).catch(errorF);
      fet(`grupy/${this.id}/classPrices`).then(ret => {
        this.setState(state => {
          return {
            ...state,
            classPricesForAllMonths: ret
          };
        });
      }).catch(errorF);
      fet(`grupy/${this.id}/season`).then(ret => {
        this.setState(state => {
          return {
            ...state,
            seasonInfo: ret
          };
        });
      }).catch(errorF);
    }
  }
  render() {
    if (this.state.error != null) {
      return /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null, "B\u0142\u0105d! ", this.state.error.text));
    }
    if (this.state.groupInfo == null) {
      return /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null));
      //return <tr><td>Wczytywanie danych grupy {this.id}...</td></tr>
    }
    if (this.state.attenderArr == null) {
      return /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null));
      //return <tr><td>Wczytywanie uczestników grupy {this.id}...</td></tr>
    }
    if (this.state.seasonInfo == null) {
      return /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null));
      //return <tr><td>Wczytywanie sezonu grupy {this.id}...</td></tr>
    }
    /*if(this.state.classPricesForAllMonths == null) {
        return <tr><td>Wczytywanie cen zajęć {this.id}...</td></tr>
    }*/

    const startingMonth = parseInt(this.state.seasonInfo['startDate'].split('-')[1]);
    const fromFirstToLatestMonth = funct => {
      for (let i = 0; i < 12; i++) {
        const monthIdx = (startingMonth - 1 + i) % 12 + 1;
        funct(monthIdx);
      }
    };
    const monthNamesEls = [];
    const monthPricesEls = [];
    const emptyMonthsNums = [];
    const setMP = (num, pr) => {
      monthNamesEls.push( /*#__PURE__*/React.createElement("th", {
        key: "nam_" + num
      }, miesiacF(num)));
      monthPricesEls.push( /*#__PURE__*/React.createElement("th", {
        key: "pri_" + num
      }, pr));
    };
    fromFirstToLatestMonth(num => {
      if (this.state.classPricesForAllMonths == null) {
        setMP(num, null);
        return;
      }
      const price = this.state.classPricesForAllMonths[num];
      if (price == 0) {
        emptyMonthsNums.push(num);
        return;
      }
      setMP(num, price);
    });
    const groupInfo = this.state.groupInfo;
    const {
      timestamp,
      idRoku,
      nazwa,
      godzinaZajecOd,
      godzinaZajecDo,
      dzienZajec,
      cenaZaJedneZajecia,
      uczestnicy
    } = groupInfo;
    const d = new Date();
    //let currentMonthNum = d.getMonth(); //0->11

    const attenderArr = this.state.attenderArr;
    const attendersRows = attenderArr.map((u, idx) => {
      const {
        id,
        imie,
        nazwisko,
        grupaPrzedszkolna,
        telefonOpiekuna,
        umowa,
        payments
      } = u;
      if (payments == null) {
        return /*#__PURE__*/React.createElement("tr", {
          key: "payments_attenderinfo_c" + this.id + "_" + idx
        }, /*#__PURE__*/React.createElement("td", null, "---"));
      }
      const paymentsBoxes = [];
      const gen = monthNum => {
        const {
          status,
          amountPaid,
          amountToPay,
          closed
        } = payments[monthNum];
        if (status == PaymentStatus.NO_CLASSES) {
          return /*#__PURE__*/React.createElement("td", {
            key: monthNum,
            className: "payments-quad-noclasses"
          });
        } else if (status == PaymentStatus.NOT_PAID) {
          return /*#__PURE__*/React.createElement("td", {
            key: monthNum,
            className: "payments-quad-notpaid"
          }, "0/", amountToPay);
        } else if (status == PaymentStatus.PAID_PARTIALLY) {
          return /*#__PURE__*/React.createElement("td", {
            key: monthNum,
            className: "payments-quad-paidpartially"
          }, amountPaid, "/", amountToPay);
        } else if (status == PaymentStatus.PAID) {
          return /*#__PURE__*/React.createElement("td", {
            key: monthNum,
            className: "payments-quad-paid" + (closed ? " payments-quad-closed" : "")
          }, amountPaid);
        } else if (status == PaymentStatus.NOT_YET_CHECKED) {
          return /*#__PURE__*/React.createElement("td", {
            key: monthNum,
            className: "payments-quad-notchecked"
          }, amountPaid == 0 ? "" : amountPaid == amountToPay ? amountPaid : amountPaid + "/" + amountToPay);
        }
      };
      fromFirstToLatestMonth(num => {
        if (emptyMonthsNums.includes(num)) {
          return;
        }
        paymentsBoxes.push(gen(num));
      });
      const isRowSelected = this.props.panel.state.selectedAttender == id;
      return /*#__PURE__*/React.createElement("tr", {
        key: "payments_attenderinfo_c" + this.id + "_" + idx,
        className: `payment-boxes${isRowSelected ? " row-selected" : ""}`
      }, paymentsBoxes);
    });
    return /*#__PURE__*/React.createElement(React.Fragment, null, /*#__PURE__*/React.createElement("tr", {
      key: "payments_payments_a" + this.id,
      className: "label"
    }, monthNamesEls), /*#__PURE__*/React.createElement("tr", {
      key: "payments_payments_b" + this.id,
      className: "label"
    }, monthPricesEls), attendersRows);
  }
}
class GroupAttendersPaymentsTableRowsAdults extends React.Component {
  state = {
    groupInfo: null,
    error: null,
    attenderArr: null,
    classPricesForAllMonths: null,
    usersPaymentInfoArr: null,
    seasonInfo: null
  };
  constructor(props) {
    super(props);
    this.id = props.groupId;
    const errorF = err => {
      console.log(err);
      this.setState({
        ...this.state,
        error: err
      });
    };
    if (this.id == 0 || this.id == 0) {
      /* SPECIAL CASE: DISCHARGED ATTENDERS */
      fet(`miejsca/${this.props.placeId}/attendersDischarged`).then(attarr => {
        this.setState({
          ...this.state,
          attenderArr: attarr,
          groupInfo: {
            nazwa: "Wypisani",
            godzinaZajecOd: "--",
            godzinaZajecDo: "--"
          }
        });
        const fetchNextUser = (index = 0) => {
          const attender = attarr[index];
          if (attender == null) {
            return;
          }
          fet(`uczestnicy/${attender['id']}/payments`).then(ret => {
            this.setState(state => {
              state.attenderArr[index]['payments'] = ret;
              return state;
            });
            fetchNextUser(index + 1);
          }).catch(errorF);
        };
        fetchNextUser();
      }).catch(errorF);
      fet(`miejsca/${this.props.placeId}/season`).then(ret => {
        this.setState(state => {
          return {
            ...state,
            seasonInfo: ret
          };
        });
      }).catch(errorF);
    } else {
      fet(`grupy/${this.id}`).then(ret => {
        this.setState(state => {
          return {
            ...state,
            groupInfo: ret
          };
        });
        setTimeout(() => {
          fet(`grupy/${this.id}/attenders`).then(attarr => {
            this.setState({
              ...this.state,
              attenderArr: attarr
            });
            const fetchNextUser = (index = 0) => {
              const attender = attarr[index];
              if (attender == null) {
                return;
              }
              fet(`uczestnicy/${attender['id']}/payments`).then(ret => {
                this.setState(state => {
                  state.attenderArr[index]['payments'] = ret;
                  return state;
                });
                fetchNextUser(index + 1);
              }).catch(errorF);
            };
            fetchNextUser();
          }).catch(errorF);
        }, 100);
      }).catch(errorF);
      fet(`grupy/${this.id}/classPrices`).then(ret => {
        this.setState(state => {
          return {
            ...state,
            classPricesForAllMonths: ret
          };
        });
      }).catch(errorF);
      fet(`grupy/${this.id}/season`).then(ret => {
        this.setState(state => {
          return {
            ...state,
            seasonInfo: ret
          };
        });
      }).catch(errorF);
    }
  }
  render() {
    if (this.state.error != null) {
      return /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null, "B\u0142\u0105d! ", this.state.error.text));
    }
    if (this.state.groupInfo == null) {
      return /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null));
      //return <tr><td>Wczytywanie danych grupy {this.id}...</td></tr>
    }
    if (this.state.attenderArr == null) {
      return /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null));
      //return <tr><td>Wczytywanie uczestników grupy {this.id}...</td></tr>
    }
    if (this.state.seasonInfo == null) {
      return /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null));
      //return <tr><td>Wczytywanie sezonu grupy {this.id}...</td></tr>
    }
    if (this.state.classPricesForAllMonths == null) {
      return /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null));
      //return <tr><td>Wczytywanie cen zajęć {this.id}...</td></tr>
    }
    const startingMonth = parseInt(this.state.seasonInfo['startDate'].split('-')[1]);
    const monthNamesEls = [];
    const monthPricesEls = [];
    const emptyMonthsNums = [];
    const biggestCycle = Math.max(...Object.keys(this.state.classPricesForAllMonths).map(p => parseInt(p)));
    for (let i = 1; i <= biggestCycle; i++) {
      const price = this.state.classPricesForAllMonths[i];
      if (price == 0) {
        emptyMonthsNums.push(i);
        return;
      }
      monthNamesEls.push( /*#__PURE__*/React.createElement("th", {
        key: "nam_" + i
      }, "cykl ", i));
      monthPricesEls.push( /*#__PURE__*/React.createElement("th", {
        key: "pri_" + i
      }, price));
    }
    const groupInfo = this.state.groupInfo;
    const {
      timestamp,
      idRoku,
      nazwa,
      godzinaZajecOd,
      godzinaZajecDo,
      dzienZajec,
      cenaZaJedneZajecia,
      uczestnicy
    } = groupInfo;
    const attenderArr = this.state.attenderArr;
    const attendersRows = attenderArr.map((u, idx) => {
      const {
        id,
        imie,
        nazwisko,
        grupaPrzedszkolna,
        telefonOpiekuna,
        umowa,
        payments
      } = u;
      if (payments == null) {
        return /*#__PURE__*/React.createElement("tr", {
          key: "payments_attenderinfo_c" + this.id + "_" + idx
        }, /*#__PURE__*/React.createElement("td", null, "---"));
      }
      const paymentsBoxes = [];
      const gen = monthNum => {
        const {
          status,
          amountPaid,
          amountToPay,
          closed
        } = payments[monthNum];
        if (status == PaymentStatus.NO_CLASSES) {
          return /*#__PURE__*/React.createElement("td", {
            key: monthNum,
            className: "payments-quad-noclasses"
          });
        } else if (status == PaymentStatus.NOT_PAID) {
          return /*#__PURE__*/React.createElement("td", {
            key: monthNum,
            className: "payments-quad-notpaid"
          }, "0/", amountToPay);
        } else if (status == PaymentStatus.PAID_PARTIALLY) {
          return /*#__PURE__*/React.createElement("td", {
            key: monthNum,
            className: "payments-quad-paidpartially"
          }, amountPaid, "/", amountToPay);
        } else if (status == PaymentStatus.PAID) {
          return /*#__PURE__*/React.createElement("td", {
            key: monthNum,
            className: "payments-quad-paid" + (closed ? " payments-quad-closed" : "")
          }, amountPaid);
        } else if (status == PaymentStatus.NOT_YET_CHECKED) {
          return /*#__PURE__*/React.createElement("td", {
            key: monthNum,
            className: "payments-quad-notchecked"
          }, amountPaid == 0 ? "" : amountPaid);
        }
      };
      for (let i = 1; i <= biggestCycle; i++) {
        if (emptyMonthsNums.includes(i)) {
          return;
        }
        paymentsBoxes.push(gen(i));
      }
      const isRowSelected = this.props.panel.state.selectedAttender == id;
      return /*#__PURE__*/React.createElement("tr", {
        key: "payments_attenderinfo_c" + this.id + "_" + idx,
        className: `payment-boxes${isRowSelected ? " row-selected" : ""}`
      }, paymentsBoxes);
    });
    return /*#__PURE__*/React.createElement(React.Fragment, null, /*#__PURE__*/React.createElement("tr", {
      key: "payments_payments_a" + this.id,
      className: "label"
    }, monthNamesEls), /*#__PURE__*/React.createElement("tr", {
      key: "payments_payments_b" + this.id,
      className: "label"
    }, monthPricesEls), attendersRows);
  }
}
class GroupAttendersPaymentsTableRowsPlace extends React.Component {
  state = {
    groupInfo: null,
    error: null,
    payments: null,
    classPricesForAllMonths: null,
    usersPaymentInfoArr: null,
    seasonInfo: null
  };
  constructor(props) {
    super(props);
    this.id = props.miejsceId;
    fet(`miejsca/${this.id}/payments`).then(ret => {
      this.setState(state => {
        state.payments = ret;
        return state;
      });
    }).catch(ret => {
      this.setState({
        ...this.state,
        error: ret
      });
    });
    fet(`miejsca/${this.id}/season`).then(ret => {
      this.setState(state => {
        return {
          ...state,
          seasonInfo: ret
        };
      });
    }).catch(ret => {
      console.log(ret);
      this.setState({
        ...this.state,
        error: ret
      });
    });
  }
  render() {
    if (this.state.error != null) {
      return /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null, "B\u0142\u0105d! ", this.state.error.text));
    }
    if (this.state.seasonInfo == null) {
      return /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null));
      //return <tr><td>Wczytywanie sezonu grupy {this.id}...</td></tr>
    }
    if (this.state.payments == null) {
      return /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null));
      //return <tr><td>Wczytywanie płatności...</td></tr>
    }
    const startingMonth = parseInt(this.state.seasonInfo['startDate'].split('-')[1]);
    const fromFirstToLatestMonth = funct => {
      for (let i = 0; i < 12; i++) {
        const monthIdx = (startingMonth - 1 + i) % 12 + 1;
        funct(monthIdx);
      }
    };
    const paymentsObj = this.state.payments;
    const monthNamesEls = [];
    const monthPricesEls = [];
    const emptyMonthsNums = [];
    fromFirstToLatestMonth(num => {
      const price = paymentsObj[num]['amountToPay'];
      if (price == 0) {
        emptyMonthsNums.push(num);
        return;
      }
      monthNamesEls.push( /*#__PURE__*/React.createElement("th", {
        key: "nam_" + num
      }, miesiacF(num)));
      monthPricesEls.push( /*#__PURE__*/React.createElement("th", {
        key: "pri_" + num
      }, price));
    });
    const paymentsBoxes = [];
    const gen = monthNum => {
      const {
        status,
        amountPaid,
        amountToPay,
        closed
      } = paymentsObj[monthNum];
      if (status == PaymentStatus.NO_CLASSES) {
        return /*#__PURE__*/React.createElement("td", {
          key: monthNum,
          className: "payments-quad-noclasses"
        });
      } else if (status == PaymentStatus.NOT_PAID) {
        return /*#__PURE__*/React.createElement("td", {
          key: monthNum,
          className: "payments-quad-notpaid"
        }, "0");
      } else if (status == PaymentStatus.PAID_PARTIALLY) {
        return /*#__PURE__*/React.createElement("td", {
          key: monthNum,
          className: "payments-quad-paidpartially"
        }, amountPaid, "/", amountToPay);
      } else if (status == PaymentStatus.PAID) {
        return /*#__PURE__*/React.createElement("td", {
          key: monthNum,
          className: "payments-quad-paid" + (closed ? " payments-quad-closed" : "")
        }, amountPaid);
      } else if (status == PaymentStatus.NOT_YET_CHECKED) {
        return /*#__PURE__*/React.createElement("td", {
          key: monthNum,
          className: "payments-quad-notchecked"
        }, amountPaid);
      }
    };
    fromFirstToLatestMonth(num => {
      if (emptyMonthsNums.includes(num)) {
        return;
      }
      paymentsBoxes.push(gen(num));
    });
    return /*#__PURE__*/React.createElement(React.Fragment, null, /*#__PURE__*/React.createElement("tr", {
      key: "payments_payments_a" + this.id,
      className: "label"
    }, monthNamesEls), /*#__PURE__*/React.createElement("tr", {
      key: "payments_payments_b" + this.id,
      className: "label"
    }, monthPricesEls), /*#__PURE__*/React.createElement("tr", {
      key: "payments_attenderinfo_c" + this.id + "_" + 0,
      className: "payment-boxes"
    }, paymentsBoxes));
  }
}
class PanelPlatnosci extends React.Component {
  state = {
    placeInfo: null,
    instrInfo: null,
    error: null,
    selectedAttender: 0,
    selectedGroup: 0
  };
  constructor(props) {
    super(props);
    this.miejsceId = modular.selectors.place;
    this.state.selectedAttender = modular.selectors.attender;
    function err(err) {
      console.log("errored ");
      this.setState({
        ...this.state,
        error: err
      });
    }
    fet(`miejsca/${this.miejsceId}?projection=withGroups`).then(ret => {
      this.setState({
        ...this.state,
        placeInfo: ret
      });
      fet(`instruktorzy/${ret.instructor}`).then(ret => {
        this.setState({
          ...this.state,
          instrInfo: ret
        });
      }).catch(err);
    }).catch(err);
  }
  setSelectedAttender = att => {
    modular.selectors.attender = att;
    this.setState(state => ({
      ...state,
      selectedAttender: att
    }));
  };
  setSelectedGroup = att => {
    this.setState(state => ({
      ...state,
      selectedGroup: att
    }));
  };
  render() {
    if (this.state.error != null) {
      return /*#__PURE__*/React.createElement("span", null, "B\u0142\u0105d! ", this.state.error);
    }
    if (this.state.placeInfo == null) {
      return /*#__PURE__*/React.createElement("span", null, "Wczytywanie...");
    }
    if (this.state.instrInfo == null) {
      return /*#__PURE__*/React.createElement("span", null, "Wczytywanie...");
    }
    const placeInfo = this.state.placeInfo;
    placeInfo.directorFirstname = undefined;
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
    }, /*#__PURE__*/React.createElement("div", {
      onClick: () => {
        //PLACE NAME CLICKED EVENT
        showDialog( /*#__PURE__*/React.createElement(DialogPlaceInfo, {
          id: placeInfo.id
        }));
      }
    }, /*#__PURE__*/React.createElement("h4", {
      className: "inf-clickable",
      onClick: () => {}
    }, placeInfo.name), /*#__PURE__*/React.createElement("span", null, placeInfo.address)), /*#__PURE__*/React.createElement("div", {
      onClick: () => {
        showDialog( /*#__PURE__*/React.createElement(DialogInstructorInfo, {
          instructorId: placeInfo.instructor
        }));
      }
    }, /*#__PURE__*/React.createElement("img", {
      src: `avatars/${instrInfo.avatar}`
    }), instrInfo == null ? null : /*#__PURE__*/React.createElement("span", {
      className: "inf-clickable"
    }, `${instrInfo.firstName} ${instrInfo.lastName} ${instrInfo.phoneNum}`)), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("img", {
      src: "images/icons/calendar.png"
    }), /*#__PURE__*/React.createElement("span", null, "dzien zajec", dzien(placeInfo.defaultClassWeekDay))), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("img", {
      src: "images/icons/group.png"
    }), /*#__PURE__*/React.createElement("span", null, "liczba os\xF3b w grupie")), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("img", {
      src: "images/icons/draw.png"
    }), /*#__PURE__*/React.createElement("span", null, "Imienne")), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("img", {
      src: "images/icons/kindergarden-children-and-teacher.png"
    }), /*#__PURE__*/React.createElement("span", null, "Imienne"))), /*#__PURE__*/React.createElement(PaymentsLegend, null)), /*#__PURE__*/React.createElement("div", {
      className: "payments-menu"
    }, /*#__PURE__*/React.createElement("div", {
      className: "payments-menu-editions"
    }, /*#__PURE__*/React.createElement(EditionsMenuBtn, {
      action: () => {
        dodajGrupe();
      }
    }, "dodaj grup\u0119"), /*#__PURE__*/React.createElement(EditionsMenuBtn, {
      action: () => {
        const gr = this.state.selectedGroup;
        if (gr == 0) {
          return;
        }
        panelEdycjiGrupyById(gr);
      }
    }, "edytuj grup\u0119"), /*#__PURE__*/React.createElement(EditionsMenuBtn, {
      action: () => {
        dodajUczestnika();
      }
    }, "dodaj uczestnika"), /*#__PURE__*/React.createElement(EditionsMenuBtn, {
      action: () => {
        const at = this.state.selectedAttender;
        if (at == 0) {
          return;
        }
        panelEdycjiUczestnikaById(at);
      }
    }, "edytuj uczestnika"), /*#__PURE__*/React.createElement(EditionsMenuBtn, {
      action: () => {
        const at = this.state.selectedAttender;
        if (at == 0) {
          return;
        }
        modular.openWiadomosci(at);
      }
    }, "konwersacja"), /*#__PURE__*/React.createElement(EditionsMenuBtn, {
      action: () => {
        const at = this.state.selectedAttender;
        if (at == 0) {
          return;
        }
        //modular.openAttenderPaymentManipulationDialog(at)
        //modular.sho
        //modular.dialogs.showDialog(modular.dialogs.types.OverdueAttenderPaymentsSMS,{})
        //modular.dialogs.list.attenderManipulatePayments(modular.selectors.attender)
        showDialog( /*#__PURE__*/React.createElement(DialogAttenderManupulatePayments, {
          attenderId: modular.selectors.attender
        }));
      }
    }, "rozliczenia"), /*#__PURE__*/React.createElement(EditionsMenuBtn, {
      action: () => {
        const at = this.state.selectedAttender;
        if (at === 0) {
          return;
        }
        showDialog( /*#__PURE__*/React.createElement(DialogAttenderManupulateGroupChanges, {
          attenderId: modular.selectors.attender
        }));
      }
    }, "przepisy"), this.state.placeInfo.type == PlaceType.TYPE_PLACE ? /*#__PURE__*/React.createElement(EditionsMenuBtn, {
      action: () => {
        historiaPlatnosciPlacowka(this.state.placeInfo['id']);
      }
    }, "p\u0142atno\u015Bci plac\xF3wki") : /*#__PURE__*/React.createElement(EditionsMenuBtn, {
      action: () => {
        const at = this.state.selectedAttender;
        if (at == 0) {
          return;
        }
        historiaPlatnosci(at);
      }
    }, "p\u0142atno\u015Bci uczestnika")))), /*#__PURE__*/React.createElement("div", {
      className: "payments-bot"
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
    }))), /*#__PURE__*/React.createElement("table", {
      className: "mega-table"
    }, /*#__PURE__*/React.createElement("tbody", null, placeType == PlaceType.TYPE_KIDS ? /*#__PURE__*/React.createElement(React.Fragment, null, placeInfo.groups.map(({
      id
    }) => /*#__PURE__*/React.createElement(GroupAttendersPaymentsTableRows, {
      panel: this,
      groupId: id,
      key: "payments_payments_rows_" + id
    })), /*#__PURE__*/React.createElement(GroupAttendersPaymentsTableRows, {
      panel: this,
      placeId: this.miejsceId,
      groupId: 0,
      key: "payments_payments_rows_wyp"
    })) : placeType == PlaceType.TYPE_ADULTS ? /*#__PURE__*/React.createElement(React.Fragment, null, placeInfo.groups.map(({
      id
    }) => /*#__PURE__*/React.createElement(GroupAttendersPaymentsTableRowsAdults, {
      panel: this,
      groupId: id,
      key: "payments_payments_rows_" + id
    })), /*#__PURE__*/React.createElement(GroupAttendersPaymentsTableRowsAdults, {
      panel: this,
      placeId: this.miejsceId,
      groupId: 0,
      key: "payments_payments_rows_wyp"
    })) : placeType == PlaceType.TYPE_PLACE ? /*#__PURE__*/React.createElement(React.Fragment, null, /*#__PURE__*/React.createElement(GroupAttendersPaymentsTableRowsPlace, {
      miejsceId: this.miejsceId,
      key: "payments_payments_rows_"
    }), /*#__PURE__*/React.createElement(GroupAttendersPaymentsTableRows, {
      placeId: this.miejsceId,
      groupId: 0,
      key: "payments_payments_rows_wyp"
    })) : null)))));
  }
}
modular.program.panels.payments = PanelPlatnosci;
export function EditionsMenuBtn(props) {
  return /*#__PURE__*/React.createElement("span", {
    className: "editions-button",
    onClick: props.action
  }, props.children);
}
function PaymentsLegend(props) {
  return /*#__PURE__*/React.createElement("div", {
    className: "legenda"
  }, /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    className: "legendaKolor",
    style: {
      backgroundColor: 'rgba(197,217,241,1)'
    }
  }), /*#__PURE__*/React.createElement("div", {
    className: "legendaText"
  }, "Zap\u0142acone")), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    className: "legendaKolor",
    style: {
      backgroundColor: 'rgba(177,160,199,1)'
    }
  }), /*#__PURE__*/React.createElement("div", {
    className: "legendaText"
  }, "P\u0142atno\u015B\u0107 niepe\u0142na")), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    className: "legendaKolor",
    style: {
      backgroundColor: 'rgba(22,54,92,1)'
    }
  }), /*#__PURE__*/React.createElement("div", {
    className: "legendaText"
  }, "Brak p\u0142atno\u015Bci")), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    className: "legendaKolor",
    style: {
      backgroundColor: 'blue'
    }
  }), /*#__PURE__*/React.createElement("div", {
    className: "legendaText"
  }, "W tym miesi\u0105cu nie by\u0142o \u017Cadnych zaj\u0119\u0107")), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    className: "legendaKolor",
    style: {
      backgroundColor: 'orange'
    }
  }), /*#__PURE__*/React.createElement("div", {
    className: "legendaText"
  }, "Interakcja")));
}
export function dzien(id) {
  switch (id) {
    case 0:
      return "poniedziałek";
    case 1:
      return "wtorek";
    case 2:
      return "środa";
    case 3:
      return "czwartek";
    case 4:
      return "piątek";
    case 5:
      return "sobota";
    case 6:
      return "niedziela";
  }
}
export function miesiacF(id) {
  switch (id) {
    case 1:
      return "styczeń";
    case 2:
      return "luty";
    case 3:
      return "marzec";
    case 4:
      return "kwiecień";
    case 5:
      return "maj";
    case 6:
      return "czerwiec";
    case 7:
      return "lipiec";
    case 8:
      return "sierpień";
    case 9:
      return "wrzesień";
    case 10:
      return "październik";
    case 11:
      return "listopad";
    case 12:
      return "grudzień";
  }
}
function umowaf(id) {
  switch (id) {
    case 0:
      return "b";
    case 1:
      return "i";
    case 2:
      return "f";
    case 3:
      return "w";
  }
}
function beautifyPhoneNum(str) {
  if (str == null) {
    return null;
  }
  if (str.length != 12) {
    return str;
  }
  return `${str.substring(0, 3)} ${str.substring(3, 6)}-${str.substring(6, 9)}-${str.substring(9, 12)}`;
}
class PaymentStatus {
  static PAID = 0;
  static PAID_PARTIALLY = 1;
  static NOT_PAID = 2;
  static NO_CLASSES = 3;
  static NOT_YET_CHECKED = 4;
}
export class PlaceType {
  static TYPE_KIDS = 0;
  static TYPE_ADULTS = 2;
  static TYPE_PLACE = 1;
}
//# sourceMappingURL=panel_platnosci.js.map