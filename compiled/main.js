import ChetniDialog from '/js/compiled/chetni_dialog.js';
import WiadomosciUczestnika from '/js/compiled/wiadomosci_dialog.js';
//import NewMessagesPinnedDialog from "./panel_messages_news.js"
import UczestnikImportDialog from "./copy_uczestnik_dialog_depre";
import CopyUczestnikLegacyDialog from "./copy_uczestnik_dialog_depre";
import { Dialog, showDialog } from "./dialog.js";
//import {ManipulateAttenderClosedMonths} from "./panel_platnosci";
//import {OverdueAttenderPaymentsSMS} from "./dialog_overdue_attenders_sms.js";

const {
  useEffect,
  useState,
  createRef,
  useCallback,
  cloneElement
} = React;
import "./panel_messages_news.js";
import "./panel_platnosci.js";
import "./panel_sms_wew.js";
import "./dialog_attender_manipulate_payments.js";
import "./dialog_overdue_attenders_sms.js";
import { OverdueAttenderPaymentsSMSDialog } from "./dialog_overdue_attenders_sms.js";
import { DialogAttenderBrowseAttendances } from "./dialog_attender_browse_attendances";
//import {OverdueAttenderPaymentsSMSDialog} from "./dialog_overdue_attenders_sms.js";
import { jsx as ___EmotionJSX } from "@emotion/react";
export function nitro(address, reactEl, injectToFieldName = "response") {
  fet(address).then(e => {
    reactEl.state[injectToFieldName] = e;
    reactEl.forceUpdate();
  }).catch(err => {
    reactEl.setState({
      error: err
    });
  });
}
export function gather(address, reactEl, injectToFieldName = "response", errorCallback = null, successCallback = null) {
  fet(address).then(e => {
    reactEl.setState(state => {
      state[injectToFieldName] = e;
      return state;
    });
    if (successCallback != null) {
      successCallback(e);
    }
  }).catch(err => {
    if (errorCallback != null) {
      errorCallback(injectToFieldName, err);
    }
  });
}
export function kon(reactEl, injectToFieldName) {
  return value => {
    reactEl.setState(state => {
      state[injectToFieldName] = value;
      return state;
    });
  };
}
export function zebra(address, success, failure, options = null) {
  fet(address, options).then(res => {
    if (success == null) {
      return;
    }
    if (success.name != "kon") {
      success(res);
      return;
    }
    success(res);
  }).catch(err => {
    if (failure == null) {
      return;
    }
    if (failure.name != "kon") {
      failure(err);
      return;
    }
    failure(err);
  });
}
modular.openClassesDetailedInfoDialog = attId => {
  showDialog( /*#__PURE__*/React.createElement(DialogAttenderBrowseAttendances, {
    attenderId: attId
  }));
};
modular.openChetni = miejsceId => {
  let el = crEl("div", "box_chetni");
  el.style.minWidth = "190px";
  let box = Dialog.build().inner(el).centered(true).title("chętni do zapisania").draw();
  const root = ReactDOM.createRoot(el);
  root.render( /*#__PURE__*/React.createElement(ChetniDialog, {
    miejsceId: miejsceId
  }));
};
modular.sendMessagesToOverdueAttenders = (places, message) => {
  showDialog( /*#__PURE__*/React.createElement(OverdueAttenderPaymentsSMSDialog, {
    placesArr: places,
    message: message
  }));
};
modular.openWiadomosci = ucz => {
  showDialog( /*#__PURE__*/React.createElement(WiadomosciUczestnika, {
    uczestnikId: ucz,
    forceReloadUczestnicyByNumber: true
  }));
};
modular.openWiadomosciByNumber = number => {
  showDialog( /*#__PURE__*/React.createElement(WiadomosciUczestnika, {
    number: number
  }));
};
// executed from phpscripts/dodawanieUczestnika/importuj z poprzedniego roku
modular.importUserDialog = (miejsceId, grupaId) => {
  zamknijPopup();
  showDialog( /*#__PURE__*/React.createElement(Dialog, {
    title: "Import uczestnika"
  }, /*#__PURE__*/React.createElement(UczestnikImportDialog, {
    miejsceId: miejsceId,
    grupaId: grupaId
  })));
};
modular.przeniesUczestnika = id => {
  showDialog( /*#__PURE__*/React.createElement(Dialog, {
    title: "Kopiowanie uczestnika"
  }, /*#__PURE__*/React.createElement(CopyUczestnikLegacyDialog, {
    uczestnikId: id
  })));
};
modular.loadHistoriaSMSTable = (el, miejsceId) => {
  const root = ReactDOM.createRoot(el);
  root.render( /*#__PURE__*/React.createElement(HistoriaSMS, {
    miejsceId: miejsceId
  }));
};
/* executed from script_legacy.js/sendWewSms() */
modular.sendMessagesToUsersMulti = (message, users) => {
  function SendingMessages({
    uczestnikId,
    title
  }) {
    let [response, setResponse] = useState(null);
    let [error, setError] = useState(null);
    if (error != null) {
      return /*#__PURE__*/React.createElement("div", {
        className: "send_messages_dialog"
      }, /*#__PURE__*/React.createElement("span", null, "Wyst\u0105pi\u0142 b\u0142\u0105d:"), /*#__PURE__*/React.createElement("span", null, error.message));
    }
    if (response == null) {
      fet("sms_groups/send", {
        method: "POST",
        body: JSON.stringify({
          "userIds": users,
          "message": message
        })
      }).then(resp => {
        setResponse(resp);
      }).catch(err => {
        setError(err);
        console.log(err);
      });
      return /*#__PURE__*/React.createElement("div", {
        className: "send_messages_dialog"
      }, /*#__PURE__*/React.createElement("span", null, "przetwarzanie..."));
    }

    //console.log(response);//TODO

    return /*#__PURE__*/React.createElement("div", {
      className: "send_messages_dialog"
    }, /*#__PURE__*/React.createElement("span", null, "Sukces!"));
  }
  showDialog( /*#__PURE__*/React.createElement(Dialog, {
    title: "Wysy\u0142anie wiadomo\u015Bci"
  }, /*#__PURE__*/React.createElement(SendingMessages, null)));
};
function getSelectedAttender() {
  return modular.selectors.attender;
}
function getCookie(name) {
  let value = "; " + document.cookie;
  let parts = value.split("; " + name + "=");
  if (parts.length === 2) return parts.pop().split(";").shift();
}
function setCookie(name, value, days) {
  let expires = "";
  if (days) {
    let date = new Date();
    date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
    expires = "; expires=" + date.toUTCString();
  }
  document.cookie = name + "=" + (value || "") + expires + "; path=/";
}
//# sourceMappingURL=main.js.map