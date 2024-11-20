//import {fet} from "../script_support"
import WiadomosciUczestnika from "./wiadomosci_dialog";
const {
  useEffect,
  useState,
  useRef,
  useCallback,
  createRef
} = React;
import { gather, kon, nitro, zebra } from "./main.js";
import { Dialog, showDialog } from "./dialog.js";

//asdf
import { jsx as ___EmotionJSX } from "@emotion/react";
class NewMessagesPinnedDialog extends React.Component {
  constructor() {
    super(undefined);
    this.state = {
      holderVisible: true,
      panelVisible: false,
      error: null,
      messageCount: -1
    };
    this.refresher = null;
    this.loadStateFromCookie();
    this.loadNewMessageCountFromServer();
    this.countChangedCallback = null;
  }
  cookie_name = "newMessagesPinnedDialogVisibility";
  loadStateFromCookie() {
    const cookie = getCookie(this.cookie_name);
    if (cookie != null) {
      const spl = cookie.split(":");
      const holder = spl[0] === "true";
      const panel = spl[1] === "true";
      this.state.holderVisible = holder;
      this.state.panelVisible = panel;
    } else {
      this.saveStateToCookie();
    }
  }
  saveStateToCookie() {
    setCookie(this.cookie_name, (this.state.holderVisible ? "true" : "false") + ":" + (this.state.panelVisible ? "true" : "false"), 365);
  }
  componentDidUpdate(prevProps, prevState, _prevContext) {}
  render() {
    const holderVisible = this.state.holderVisible;
    const panelVisible = this.state.panelVisible;
    if (this.state.error != null) {
      return /*#__PURE__*/React.createElement("span", null, "err! ", this.state.error.message);
    }
    if (this.state.messageCount === -1) {
      this.loadNewMessageCountFromServer();
    }
    const popup_newmessages_classes = ["popup_newmessages"];
    if (!holderVisible) {
      popup_newmessages_classes.push("hidden");
    }
    /*if(this.state.shouldBlink) {
        popup_newmessages_classes.push("pulse-anim-single")
    }*/

    const messageCount = this.state.messageCount;
    // counterDecreaser={this.decreaseCounter.bind(this)} globalMessageCounterUpdater={this.loadNewMessageCountFromServer.bind(this)}
    return /*#__PURE__*/React.createElement("div", {
      className: "newmessages_ui_container"
    }, holderVisible && panelVisible ? /*#__PURE__*/React.createElement(NewMessagesPanel, {
      holder: this
    }) : null, /*#__PURE__*/React.createElement("div", {
      className: "newmessages_holder"
    }, /*#__PURE__*/React.createElement("div", {
      className: "hide_button " + (!holderVisible && messageCount > 0 ? "pulse-anim" : ""),
      onClick: this.flipHolderVisibility.bind(this)
    }, /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("i", {
      className: "fa-solid fa-angle-" + (holderVisible ? "left" : "right")
    }))), /*#__PURE__*/React.createElement("div", {
      className: popup_newmessages_classes.join(" "),
      onClick: this.flipPanelVisibility.bind(this)
    }, /*#__PURE__*/React.createElement("span", null, "Nowe wiadomo\u015Bci ", messageCount > 0 ? `(${messageCount})` : ""), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("i", {
      className: "fa-solid fa-angle-" + (panelVisible ? "down" : "up")
    })))));
  }
  loadNewMessageCountFromServer() {
    if (this.refresher == null) {
      this.refresher = setInterval(() => {
        this.loadNewMessageCountFromServer();
      }, 2000);
    }
    /*fet("sms/unread_sms_count").then(res=>{
        const newCount = parseInt(res);
        if(newCount !== this.state.messageCount && this.state.messageCount !== -1) {
            if(this.countChangedCallback != null) {
                this.countChangedCallback()
            }
        }
        this.setState({messageCount: newCount})
    }).catch(err=>{
        this.setState({error: parseInt(err.toString())})
    })*/
    zebra("sms/unreadSMSCount", res => {
      const newCount = parseInt(res);
      if (newCount !== this.state.messageCount && this.state.messageCount !== -1) {
        if (this.countChangedCallback != null) {
          this.countChangedCallback();
        }
      }
      this.setState({
        messageCount: newCount
      });
    }, kon(this, "error"));
  }
  decreaseCounter(amount) {
    this.setState({
      messageCount: this.state.messageCount - amount
    });
  }
  flipHolderVisibility() {
    this.state.holderVisible = !this.state.holderVisible;
    this.saveStateToCookie();
    this.forceUpdate();
  }
  flipPanelVisibility() {
    this.state.panelVisible = !this.state.panelVisible;
    this.saveStateToCookie();
    this.forceUpdate();
  }
}
modular.showMessagesPanel = () => {
  if (!is_admin) {
    return;
  }
  modular.dialogs.showRenderedDialog( /*#__PURE__*/React.createElement(NewMessagesPinnedDialog, null));
};
class NewMessagesPanel extends React.Component {
  constructor({
    holder
  }) {
    super(undefined); //counterDecreaser,globalMessageCounterUpdater

    this.state = {
      messages: null,
      error: null,
      searchResults: null,
      searchResultsUcz: null
    };
    this.holder = holder;
    holder.countChangedCallback = () => {
      this.loadMessagesFromServer();
    };
    this.loadMessagesFromServer();
    this.refresherInterval = setInterval(() => {
      zebra("sms/chid", chidServer => {
        if (this.chid == -1) {
          this.chid = chidServer;
          return;
        }
        if (this.chid == chidServer) {
          return;
        }
        this.chid = chidServer;
        this.loadMessagesFromServer();
      }, kon(this, "error"));
    }, 2000);
  }
  chid = -1;
  refresherInterval = null;
  curMessPart = 1;
  loadMoreMessages = () => {
    if (this.state.loadingMore) {
      return;
    }
    this.setState((state, props) => {
      return {
        ...state,
        loadingMore: true
      };
    });
    zebra(`sms/news`, res => {
      this.setState((state, props) => {
        return {
          ...state,
          messages: state.messages.concat(res),
          loadingMore: false
        };
      });
    }, kon(this, "error"), {
      method: "POST",
      body: {
        index: this.curMessPart,
        count: 1
      }
    });
    this.curMessPart = this.curMessPart + 1;
  };
  loadMessagesFromServer() {
    zebra(`sms/news`, kon(this, "messages"), kon(this, "error"), {
      method: "POST",
      body: {
        index: 0,
        count: this.curMessPart
      }
    });
  }
  render() {
    if (this.state.error != null) {
      return /*#__PURE__*/React.createElement("span", null, "err! ", this.state.error);
    }
    if (this.state.messages == null) {
      return /*#__PURE__*/React.createElement("div", {
        className: "newmessages_panel"
      }, /*#__PURE__*/React.createElement("span", null, "Wczytywanie..."), /*#__PURE__*/React.createElement("input", {
        placeholder: "Szukaj uczestnika"
      }));
    }
    let messages_els = null;
    if (this.state.searchResults != null) {
      messages_els = this.state.searchResults.map((foundObj, idx) => {
        const {
          attender,
          unreadSMSCount,
          personalInfo,
          parentInfo,
          place
        } = foundObj;
        const id = attender.id;
        const {
          firstName,
          lastName
        } = personalInfo;
        const phoneNum = parentInfo.number;
        const hasNewMessages = unreadSMSCount > 0;
        let tit = /*#__PURE__*/React.createElement("span", null, `[${place.name}] ${firstName} ${lastName} ${phoneNum}`);
        function userClicked() {
          /*number={phoneNum} forceReloadUczestnicyByNumber={true}*/
          showDialog( /*#__PURE__*/React.createElement(WiadomosciUczestnika, {
            uczestnikId: id
          }));
          // uczestnicy={attenders}

          if (hasNewMessages) {
            this.state.messages[idx]["unreadSMSCount"] = 0;
            this.forceUpdate();
            this.holder.decreaseCounter(unreadSMSCount);
            this.holder.forceUpdate();
          }
        }
        return /*#__PURE__*/React.createElement("div", {
          className: "message_pan" + (hasNewMessages ? " pulse-anim-single" : ""),
          onClick: userClicked.bind(this),
          key: id
        }, /*#__PURE__*/React.createElement("div", null, tit), hasNewMessages ? /*#__PURE__*/React.createElement("span", {
          className: "newmessages_count"
        }, unreadSMSCount) : "");
      });
    } else {
      messages_els = this.state.messages.map((sms, idx) => {
        const {
          attenders,
          changeItterator,
          latestMessage,
          number,
          unreadMessagesCount
        } = sms;

        //const {id:id_sms,type,status,read_status,content,number,created_timestamp,sent_timestamp,attenders,unreadSMSCount} = sms
        const hasNewMessages = unreadMessagesCount > 0;
        let tit = /*#__PURE__*/React.createElement("span", null, number);
        if (attenders != null && attenders.length > 0) {
          tit = [];
          const re_mapped = [];
          for (let i = 0; i < attenders.length; i++) {
            const u = attenders[i];
            const {
              id,
              imie,
              nazwisko,
              place
            } = u;
            const place_name = place.name;
            function fixN(n) {
              return n.toLowerCase().replaceAll(" ", "");
            }
            const found = re_mapped.find(f => fixN(f['imie']) === fixN(imie) && fixN(f['nazwisko']) === fixN(nazwisko));
            if (found != null) {
              if (found.place.name == place_name) {
                u["hidden"] = true;
              } else {
                u["placeOnly"] = true;
              }
            }
            re_mapped.push(u);
          }
          tit = re_mapped.map(({
            id,
            imie,
            nazwisko,
            place,
            placeOnly,
            hidden
          }, idx) => {
            if (hidden) {
              return null;
            }
            if (placeOnly) {
              return /*#__PURE__*/React.createElement("span", {
                key: idx
              }, "[", `${place.name}`, "]");
            }
            return /*#__PURE__*/React.createElement("span", {
              key: idx
            }, `[${place.name}] ${imie} ${nazwisko}`);
          });
          attenders.forEach(u => u['hidePlace'] = false);
          attenders.forEach(u => u['hidden'] = false);
        }
        function userClicked() {
          showDialog( /*#__PURE__*/React.createElement(WiadomosciUczestnika, {
            number: number,
            attenders: attenders
          }));
          if (hasNewMessages) {
            this.state.messages[idx]["unreadMessagesCount"] = 0;
            this.forceUpdate();
            this.holder.decreaseCounter(unreadMessagesCount);
            this.holder.forceUpdate();
          }
        }
        return /*#__PURE__*/React.createElement("div", {
          className: "message_pan" + (hasNewMessages ? " pulse-anim-single" : ""),
          onClick: userClicked.bind(this),
          key: latestMessage.id
        }, /*#__PURE__*/React.createElement("div", null, tit), hasNewMessages ? /*#__PURE__*/React.createElement("span", {
          className: "newmessages_count"
        }, unreadMessagesCount) : "");
      });
    }
    return /*#__PURE__*/React.createElement("div", {
      className: "newmessages_panel"
    }, /*#__PURE__*/React.createElement("div", null, messages_els, /*#__PURE__*/React.createElement("div", {
      className: "load_more",
      onClick: this.loadMoreMessages
    }, this.state.searchResults != null ? null : /*#__PURE__*/React.createElement("span", null, this.state.loadingMore ? "czekaj..." : "wczytaj więcej"))), /*#__PURE__*/React.createElement("input", {
      placeholder: "Szukaj uczestnika",
      onChange: this.searchboxValueChanged
    }));
  }
  searchboxInputEL = createRef();
  currentSearchId = -1;
  currentSearchTypeId = -1;
  searchboxValueChanged = event => {
    const panel = this;
    const val = event.target.value;
    if (val.length < 1) {
      this.setState({
        searchResults: null
      });
      return;
    }
    const randex = Math.random();
    this.currentSearchTypeId = randex;
    setTimeout(() => {
      if (panel.currentSearchTypeId !== randex) {
        return;
      }
      const rande = Math.random();
      panel.currentSearchId = rande;
      zebra("sms/news/find", res => {
        if (panel.currentSearchId !== rande) {
          return;
        }
        panel.setState({
          searchResults: res
        });
      }, null, {
        method: "POST",
        body: {
          str: val
        }
      });
    }, 200);
  };
}
//# sourceMappingURL=panel_messages_news.js.map