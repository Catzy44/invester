import { Dialog, showDialog } from "./dialog.js";
import { jsx as ___EmotionJSX } from "@emotion/react";
class OverdueAttenderPaymentsSMS extends React.Component {
  state = {
    running: false,
    justFinished: false
  };
  constructor(props) {
    super(props);
    this.placesArr = props.placesArr;
    this.message = props.message;
  }
  running = false;
  render() {
    let infoEl = /*#__PURE__*/React.createElement(React.Fragment, null, "wybrane miejsca: ", this.placesArr.length);
    if (this.workers != null) {
      const totalCount = this.workers.length;
      const finishedCount = this.workers.filter(worker => worker.finished).length;
      if (this.state.running) {
        infoEl = /*#__PURE__*/React.createElement(React.Fragment, null, "uko\u0144czono: ", finishedCount, "/", totalCount);
      }
      if (finishedCount == totalCount) {
        this.setState(state => ({
          ...state,
          running: false,
          justFinished: true
        }));
      }
      if (this.state.justFinished) {
        infoEl = /*#__PURE__*/React.createElement(React.Fragment, null, "uko\u0144czono!");
      }
    }
    return /*#__PURE__*/React.createElement("div", {
      className: "dialog_overdue_messages"
    }, /*#__PURE__*/React.createElement("span", {
      style: {
        maxWidth: "200px"
      }
    }, "Funkcja spowoduje wys\u0142anie wiadomo\u015Bci do wszystkich uczestnik\xF3w zalegaj\u0105cych z p\u0142atno\u015Bciami w wybranych miejscach"), /*#__PURE__*/React.createElement("span", {
      style: {
        maxWidth: "200px"
      }
    }, "Procesu nie nale\u017Cy przerywa\u0107, mo\u017Ce on zaj\u0105\u0107 do kilku minut"), infoEl, /*#__PURE__*/React.createElement("input", {
      disabled: this.state.running,
      type: "button",
      value: this.state.running ? "w toku..." : "rozpocznij",
      onClick: this.begin
    }));
  }
  workers = null;
  begin = () => {
    this.setState(state => ({
      ...state,
      running: true
    }));
    this.workers = this.placesArr.map(placeId => {
      return {
        id: placeId,
        finished: false,
        running: false,
        errored: false
      };
    });
    this.workers.forEach(worker => {
      fet(`places/${worker.id}/payments/sendSMSesToOverdueAttenders`, {
        method: "POST",
        body: {
          content: this.message
        }
      }).then(res => {
        worker.running = false;
        worker.finished = true;
        this.render();
      }).catch(err => {
        worker.running = false;
        worker.finished = true;
        worker.errored = true;
        console.log(err);
      });
    });
  };
}
export function OverdueAttenderPaymentsSMSDialog(props) {
  return /*#__PURE__*/React.createElement(Dialog, {
    title: "zaleg\u0142e p\u0142atno\u015Bci"
  }, /*#__PURE__*/React.createElement(OverdueAttenderPaymentsSMS, props));
}
//# sourceMappingURL=dialog_overdue_attenders_sms.js.map