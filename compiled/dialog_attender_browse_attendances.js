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
export class DialogAttenderBrowseAttendances extends React.Component {
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
    const reactEl = this;
    const errorF = err => {
      console.log(err);
      this.setState({
        ...this.state,
        error: err
      });
    };
    gather(`uczestnicy/${this.attenderId}/season`, this, "season", errorF);
    gather(`uczestnicy/${this.attenderId}/groupChanges`, this, "groupChanges", errorF, agcs => {
      agcs.forEach(agc => {
        fet(`groupChanges/${agc.id}/classes`, null).then(res => {
          reactEl.setState(oldState => {
            oldState.groupChanges = oldState.groupChanges.map(foundGc => {
              if (foundGc.id === agc.id) {
                foundGc.classes = res;
              }
              return foundGc;
            });
            return oldState;
          });
        }).catch(err => {});
      });
    });
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
    if (this.state.season == null) return null;
    if (this.state.groupChanges == null) return null;
    if (this.state.place == null) return null;
    const groupchangesRows = this.state.groupChanges.map(gc => {
      const {
        id,
        idRoku,
        group,
        active,
        dateJoined,
        dateLeft,
        classes
      } = gc;
      const monthNamesEls = [];
      const monthClassesEls = [];
      if (classes != null) {
        const endDate = new Date(this.state.season.endDate);
        const startDate = new Date(this.state.season.startDate);
        for (; startDate < endDate; startDate.setMonth(startDate.getMonth() + 1)) {
          const month = startDate.getMonth() + 1;
          const classesInThisMonth = classes.filter(cl => {
            const classDate = new Date(cl.date);
            const classMonth = classDate.getMonth() + 1;
            return classMonth === month;
          });
          const classesInThisMonthEls = classesInThisMonth.map(cl => {
            const classDate = new Date(cl.date);
            const classDay = classDate.getDate();
            const presences = cl.presences;
            if (presences == null) {
              return null;
            }
            const thisAttenderPresence = presences.findLast(pres => pres.attender = this.attenderId);
            function Presence(props) {
              const [presence, setPresence] = useState(props.presence);
              let color = null;
              const presenceInt = presence?.presence;
              if (presenceInt === 0) {
                color = "white";
              } else if (presenceInt === 1) {
                color = "#7ac63a";
              } else if (presenceInt === 0) {
                color = "grey";
              } else if (presenceInt === 0) {
                color = "red";
              } else if (presenceInt === 0) {
                color = "orange";
              } else if (presenceInt === 0) {
                color = "white";
              }
              return /*#__PURE__*/React.createElement("div", {
                className: "presence_div",
                style: {
                  backgroundColor: color
                },
                onClick: () => {
                  const newPresenceInt = presenceInt === 0 ? 1 : presenceInt === 1 ? 2 : 0;
                  if (presence === null) {
                    fet("presences", {
                      method: "POST",
                      body: {
                        attenderId: props.attenderId,
                        classId: props.class.id,
                        presence: 1
                      }
                    }).then(res => {
                      setPresence(res);
                    }).catch(err => {});
                  } else {
                    fet("presences", {
                      method: "PATCH",
                      body: {
                        id: presence.id,
                        presence: newPresenceInt
                      }
                    }).then(res => {
                      setPresence(res);
                    }).catch(err => {});
                  }
                }
              }, classDay);
            }
            return /*#__PURE__*/React.createElement(Presence, {
              presence: thisAttenderPresence,
              attenderId: this.attenderId,
              class: cl
            });
          });
          if (classesInThisMonth.length > 0) {
            monthNamesEls.push( /*#__PURE__*/React.createElement("th", {
              key: month
            }, miesiacF(month)));
            monthClassesEls.push( /*#__PURE__*/React.createElement("td", {
              key: month,
              className: "month_classes_cell"
            }, /*#__PURE__*/React.createElement("div", null, classesInThisMonthEls)));
          }
        }
      }
      const tab = monthNamesEls.length === 0 ? /*#__PURE__*/React.createElement("span", null, "\u0142aduje...") : /*#__PURE__*/React.createElement("table", {
        className: "months_table"
      }, /*#__PURE__*/React.createElement("tbody", null, /*#__PURE__*/React.createElement("tr", {
        key: "names"
      }, monthNamesEls), /*#__PURE__*/React.createElement("tr", {
        key: "classes"
      }, monthClassesEls)));
      return /*#__PURE__*/React.createElement("tr", {
        key: group.id
      }, /*#__PURE__*/React.createElement("td", null, group.name), /*#__PURE__*/React.createElement("td", null, dateJoined), /*#__PURE__*/React.createElement("td", null, dateLeft), /*#__PURE__*/React.createElement("td", null, tab));
    });
    const attender = this.state.attender;
    return /*#__PURE__*/React.createElement(Dialog, {
      ref: this.dialog,
      title: `obecności uczestnika [${attender.imie} ${attender.nazwisko}]`
    }, /*#__PURE__*/React.createElement("div", {
      className: "dialog_attender_browser_attendances"
    }, /*#__PURE__*/React.createElement("table", {
      className: "big_table"
    }, /*#__PURE__*/React.createElement("tbody", null, /*#__PURE__*/React.createElement("tr", {
      key: "tit"
    }, /*#__PURE__*/React.createElement("th", null, "grupa"), /*#__PURE__*/React.createElement("th", null, "data do\u0142\u0105czenia"), /*#__PURE__*/React.createElement("th", null, "data opuszczenia"), /*#__PURE__*/React.createElement("th", null, "zaj\u0119cia")), groupchangesRows))));
  }
}

/*setTimeout(()=>{
    showDialog(<DialogAttenderManupulatePayments attenderId={9821}/>)
},1000)*/
//# sourceMappingURL=dialog_attender_browse_attendances.js.map