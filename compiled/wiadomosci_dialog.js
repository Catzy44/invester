import { kon, nitro, zebra } from "./main.js";
import { Dialog, showDialog } from "./dialog.js";
import { jsx as ___EmotionJSX } from "@emotion/react";
const {
  useEffect,
  useState,
  useRef,
  useCallback,
  createRef
} = React;
let dialogList = [];
const OUT = 0;
const IN = 1;
const AWAITING = 0;
const SENT = 1;
const ERRORED = 2;
const UNREAD = 0;
const READ = 1;
class NoPhoneConnectedFrag extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      response: -1,
      error: null,
      refresher: null
    };
    this.loadDataFromServer();
    this.state.refresher = setInterval(() => {
      this.loadDataFromServer();
    }, 10000);
  }
  loadDataFromServer() {
    nitro("sms/toSend/seconds_passed_till_last", this);
  }
  componentWillUnmount() {
    if (this.state.refresher != null) {
      clearInterval(this.state.refresher);
    }
  }
  render() {
    const {
      error,
      response
    } = this.state;
    if (error != null) {
      return /*#__PURE__*/React.createElement("div", {
        className: "app_disconnected_warning"
      }, /*#__PURE__*/React.createElement("i", {
        className: "fa-solid fa-exclamation"
      }));
    }
    if (response == null || response === -1) {
      return null;
    }
    if (response < 60) {
      return null;
    }
    return /*#__PURE__*/React.createElement("div", {
      className: "app_disconnected_warning"
    }, /*#__PURE__*/React.createElement("div", null, "Aplikacja SMS jest wy\u0142\u0105czona", /*#__PURE__*/React.createElement("br", null), "wiadomo\u015Bci nie zostan\u0105 wys\u0142ane"), /*#__PURE__*/React.createElement("i", {
      className: "fa-solid fa-exclamation"
    }));
  }
}
class WiadomosciUczestnika extends React.Component {
  textAreaRef = null;
  dialogRef = null;
  forceReloadAttListByPhoneNumOnLoad = false;
  constructor(props) {
    super(props);
    const {
      number,
      attenders,
      uczestnikId,
      title,
      dialogCloseHandler,
      callbackSetter,
      forceReloadUczestnicyByNumber = false
    } = props;
    this.forceReloadAttListByPhoneNumOnLoad = forceReloadUczestnicyByNumber;
    this.dialogTitleUpdater = title;
    this.callbackSetter = callbackSetter;
    this.number = number;
    this.attenders = attenders;
    this.uczestnikId = uczestnikId;
    this.state = {
      wiadomosci: null,
      error: null
    };
    this.title_set = false;

    //allow only one instance
    const selfhashstr = this.hashstr();
    const finding = dialogList.findIndex(d => d.hashstr() === selfhashstr);
    if (finding !== -1) {
      //dialogCloseHandler(true)
      console.log("FINDING EXIT");
      this.props.root.unmount();
      return;
    }
    dialogList.push(this);
    this.instanceSecured = true;
    this.textAreaRef = createRef();
    this.messagesContainerEl = createRef();
    this.dialogRef = createRef();
    setTimeout(() => {
      this.loadMessagesFromServer();
    }, 50);
  }
  hashstr() {
    //return this.uczestnikId+""+this.number;
    return this.number;
  }
  componentWillUnmount() {
    clearInterval(this.refresher);
    if (!this.instanceSecured) {
      return;
    }
    const selfhashstr = this.hashstr();
    dialogList = dialogList.filter(d => d.hashstr() !== selfhashstr);
  }
  componentDidUpdate(prevProps, prevState, _prevContext) {
    if (this.messagesContainerEl == null || this.messagesContainerEl.current == null) {
      return;
    }
    const el = this.messagesContainerEl.current;
    el.scrollTo(0, el.scrollHeight);
  }
  render() {
    if (this.state.error != null) {
      return /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("span", null, "b\u0142\u0105d!"), /*#__PURE__*/React.createElement("span", null, this.error));
    }
    return /*#__PURE__*/React.createElement(Dialog, {
      title: "Chat uczestnika",
      blacky: true,
      ref: this.dialogRef,
      onClose: () => {
        this.props.root.unmount();
      }
    }, /*#__PURE__*/React.createElement("div", {
      className: "wiadomosci"
    }, this.state.wiadomosci == null ? null : /*#__PURE__*/React.createElement("div", {
      ref: this.messagesContainerEl,
      className: "messages_container"
    }, this.contenerizeMessages()), /*#__PURE__*/React.createElement("div", {
      className: "bot"
    }, /*#__PURE__*/React.createElement("textarea", {
      ref: this.textAreaRef,
      onKeyDown: this.taKeyDown.bind(this),
      placeholder: "Aa",
      rows: "1"
    }), /*#__PURE__*/React.createElement("div", {
      onClick: () => {
        this.send();
      }
    }, /*#__PURE__*/React.createElement("i", {
      className: "fa-solid fa-chevron-right"
    }))), /*#__PURE__*/React.createElement(NoPhoneConnectedFrag, null)));
  }
  contenerizeMessages() {
    let wiadomosci = this.state.wiadomosci;
    const generateStatusClass = function (status) {
      if (status === 0) {
        return "waiting";
      } else if (status === 0) {
        return "errored";
      }
    };
    let last_message_type = -1;
    let last_message_date = null;
    let element_arr_i = 0;
    let element_arr = [];
    let messages_group = [];
    let messages_group_inc = 0;
    function saveMessagesGroup(cur_messages_group, cur_group_type, cur_group_date) {
      let classes = [];
      classes.push(last_message_type === 0 ? "out" : "in");
      element_arr.push( /*#__PURE__*/React.createElement("div", {
        key: element_arr_i,
        className: classes.join(" ")
      }, cur_messages_group));
      element_arr_i++;
      messages_group_inc = 0;
      messages_group = [];
      if (last_message_type === -1 || last_message_type !== cur_group_type) last_message_type = cur_group_type;
      if (last_message_date == null || last_message_date !== cur_group_date) last_message_date = cur_group_date;
    }
    function addMessageToCurGroup(sms) {
      const {
        createdTimestamp,
        content,
        id,
        number,
        readStatus,
        sentTimestamp,
        status,
        type
      } = sms;
      function ReadStatusSwitcher(props) {
        const [innReadStatus, setInnReadStatus] = useState(readStatus);
        const [isProcessing, setIsProcessing] = useState(false);
        function changeReadStatus(newStatus) {
          setIsProcessing(true);
          zebra(`sms`, patchedSms => {
            setIsProcessing(false);
            setInnReadStatus(patchedSms.readStatus);
          }, null, {
            method: "PATCH",
            body: {
              id: id,
              readStatus: newStatus
            }
          });
        }
        return /*#__PURE__*/React.createElement("span", {
          className: `clickable${isProcessing ? " processing" : ""}`,
          onClick: () => {
            changeReadStatus(innReadStatus === READ ? UNREAD : READ);
          }
        }, innReadStatus === READ ? "cofnij odczytanie" : "odczytaj");
      }
      const ctExpl = createdTimestamp?.split("T");
      const date = ctExpl?.[0];
      const time = ctExpl?.[1]?.split("\.")[0];
      messages_group.push( /*#__PURE__*/React.createElement("div", {
        className: generateStatusClass(sms.status),
        key: messages_group_inc,
        onClick: event => {
          const targetElement = event.target;
          targetElement.classList.add("clickd");
        }
      }, content == null || content.length === 0 ? "[pusta]" : content, /*#__PURE__*/React.createElement("div", {
        className: "message_details"
      }, /*#__PURE__*/React.createElement("div", {
        className: "details_top"
      }, /*#__PURE__*/React.createElement("span", null, "szczeg\xF3\u0142y"), /*#__PURE__*/React.createElement("i", {
        className: "fa-solid fa-xmark popup-close-btn",
        onClick: event => {
          //CLOSE MESSAGE DETAILS BUTTON (X)
          const targetElement = event.target;
          const tcl = targetElement.parentNode.parentNode.parentNode;
          tcl.classList.remove("clickd");
        }
      })), /*#__PURE__*/React.createElement("span", null, "data: ", date), /*#__PURE__*/React.createElement("span", null, "godzina: ", time), type === OUT ? /*#__PURE__*/React.createElement("span", null, "status: ", status === AWAITING ? "czeka" : status === SENT ? "wysłana" : "błąd") : null, type === IN ? /*#__PURE__*/React.createElement(ReadStatusSwitcher, null) : null))); //add messages to messages group
      messages_group_inc++;
    }
    for (let i = 0; i < wiadomosci.length; i++) {
      const sms = wiadomosci[i];
      const type = sms["type"];
      const ct = sms["createdTimestamp"];
      const date = ct == null ? null : ct.split("T")[0];
      if (i === 0) {
        //FIRST MESSEAGE IN CHAT
        element_arr.push( /*#__PURE__*/React.createElement("div", {
          key: element_arr_i,
          className: "date"
        }, "Pocz\u0105tek konwersacji"));
        element_arr_i++;
        element_arr.push( /*#__PURE__*/React.createElement("div", {
          key: element_arr_i,
          className: "date"
        }, date));
        element_arr_i++;
        addMessageToCurGroup(sms);
        last_message_type = type;
        if (wiadomosci.length === 1) {
          saveMessagesGroup(messages_group, type, date);
        }
      } else {
        //SECOND MESSAGE IN CHAT, OR THIRD, OR LAST...
        if (last_message_type !== type) {
          //current message type differs from last messages type
          saveMessagesGroup(messages_group, type, date);
        }
        if (last_message_date !== date) {
          //current message date differs from last messages date
          saveMessagesGroup(messages_group, type, date);
          element_arr.push( /*#__PURE__*/React.createElement("div", {
            key: element_arr_i,
            className: "date"
          }, date));
          element_arr_i++;
        }
        addMessageToCurGroup(sms);
        if (i === wiadomosci.length - 1) {
          //last message in chat
          saveMessagesGroup(messages_group, type, date);
        }
      }
    }
    return element_arr;
  }
  chid = -1;
  markAllAsReadModuleIsFirstRun = true;
  loadMessagesFromServer() {
    if (this.number == null || this.forceReloadAttListByPhoneNumOnLoad) {
      this.forceReloadAttListByPhoneNumOnLoad = false;
      fet(`uczestnicy/${this.uczestnikId}/with_miejsce`).then(u => {
        this.attenders = [u];
        this.number = u['telefonOpiekuna'];
        if (this.number == null) {
          this.setState({
            error: "BRAK NUMERU!"
          });
          return;
        }
        this.loadMessagesFromServer();
      }).catch(err => {
        this.setState({
          error: err
        });
        throw err;
      });
      //return
    }
    fet(`sms/by_number`, {
      method: "POST",
      body: JSON.stringify({
        number: this.number
      })
    }).then(messageArr => {
      if (this.markAllAsReadModuleIsFirstRun) {
        this.markAllAsReadModuleIsFirstRun = false;
        if (messageArr != null && messageArr.length > 0) {
          const latestReceivedSMS = messageArr.findLast(sms => sms.type === IN && sms.readStatus === UNREAD);
          if (latestReceivedSMS != null) {
            //LATEST RECEIVED SMS ISM THERE AND IS UNREAD!
            //mark all as read (SERVER SIDE)
            fet(`sms/by_number/mark_all_read`, {
              method: "POST",
              body: JSON.stringify({
                number: this.number
              })
            }, null, false);
            //mark all as read (CLIENT SIDE)
            messageArr = messageArr.map(sms => {
              if (sms.readStatus === UNREAD) {
                sms.readStatus = READ;
              }
              return sms;
            });
          }
        }
      }
      this.setState({
        wiadomosci: messageArr
      });
    }).catch(err => {
      this.setState({
        error: err
      });
    });
    if (this.attenders == null) {
      this.dialogRef.current.setTitle(this.number);
    } else {
      const activeSeasonId = getActiveSeason(); //LEGACY!

      const els = this.attenders.map((u, idx) => {
        const {
          id,
          imie,
          nazwisko,
          place,
          season
        } = u;
        const seasonId = season != null && season.id != null ? season.id : null;
        const showUczestnik = () => {
          jumpToAttender(seasonId, place.id, id);
        };
        const isCurrentSeason = activeSeasonId == seasonId;
        const seasonName = season != null && season.label != null ? `[${season.label}]` : "";
        return /*#__PURE__*/React.createElement("span", {
          className: "click" + (isCurrentSeason ? "" : " not_curr_season"),
          onClick: showUczestnik,
          key: idx
        }, `${seasonName} [${place.name}] ${imie} ${nazwisko}`);
      });
      this.dialogRef.current.setTitle( /*#__PURE__*/React.createElement("div", null, els));
    }
    if (this.refresher == null) {
      this.refresher = setInterval(ref => {
        if (this.state.wiadomosci != null) {
          zebra("sms/by_number/chid", chidServer => {
            if (this.chid == -1) {
              this.chid = chidServer;
              return;
            }
            if (this.chid == chidServer) {
              return;
            }
            this.chid = chidServer;
            this.loadMessagesFromServer();
          }, kon(this, "error"), {
            method: "POST",
            body: JSON.stringify({
              number: this.number
            })
          });
          return;
        }
        this.loadMessagesFromServer();
      }, 2000);
    }
  }
  taKeyDown(ev) {
    if (this.textAreaRef.current == null) {
      return;
    }
    const el = this.textAreaRef.current;
    let key = ev.keyCode;
    if (ev["shiftKey"]) {
      return;
    }
    if (key !== 13) {
      return;
    }
    if (ev["ctrlKey"]) {
      el.value = `${el.value}\n`;
      this.fixtextarea(el);
      return;
    }
    /**/

    this.send();
  }
  send() {
    if (this.textAreaRef.current == null) {
      return;
    }
    const el = this.textAreaRef.current;
    const value = el.value;
    if (value.trim().length === 0) {
      return;
    }
    fet(`sms`, {
      method: "POST",
      body: JSON.stringify({
        type: 0,
        status: 0,
        readStatus: 0,
        content: el.value,
        number: this.number
      })
    }).then(packet => {
      el.value = "";
      this.fixtextarea(el);
      this.state.wiadomosci.push(packet);
      this.forceUpdate();
    });
  }
  fixtextarea(element) {
    element.style.height = 0;
    element.style.height = element.scrollHeight - 8 + "px";
  }
}
export default WiadomosciUczestnika;
//# sourceMappingURL=wiadomosci_dialog.js.map