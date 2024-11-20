import { Dialog, showDialog } from "./dialog.js";
import { miesiacF, PlaceType } from "./panel_platnosci.js";
const {
  useEffect,
  useState,
  createRef,
  useCallback,
  cloneElement
} = React;
export class DialogInstructorsManage extends React.Component {
  state = {
    instructors: null
  };
  constructor(props) {
    super(props);
    const errorF = err => {
      console.log(err);
      injectRField(this, "error")(err);
    };
    gather(`instructors`, injectRField(this, "instructors"), errorF);
  }
  render() {
    return /*#__PURE__*/React.createElement(Dialog, {
      ref: this.dialog,
      title: "rozliczenia uczestnika"
    }, /*#__PURE__*/React.createElement("div", {
      className: "dialog_attender_manipulate_payments"
    }, /*#__PURE__*/React.createElement("table", null, /*#__PURE__*/React.createElement("tbody", null, /*#__PURE__*/React.createElement("tr", {
      key: "tit"
    }, /*#__PURE__*/React.createElement("th", null, "imi\u0119"), /*#__PURE__*/React.createElement("th", null, "nazwisko"), /*#__PURE__*/React.createElement("th", null, "telefon"), /*#__PURE__*/React.createElement("th", null, "login"), /*#__PURE__*/React.createElement("th", null, "has\u0142o"), /*#__PURE__*/React.createElement("th", null, "dost\u0119pne sezony"), /*#__PURE__*/React.createElement("th", null, "rodzaj"), /*#__PURE__*/React.createElement("th", null, "avatar"), /*#__PURE__*/React.createElement("th", null, "admin"), /*#__PURE__*/React.createElement("th", null, "ukryty")), this.state.instructors.map((id, timestamp, idRoku, deleted, avatar, firstName, secondName, phoneNum, accesableYears, nick, admin, hidden, type) => {
      return /*#__PURE__*/React.createElement("tr", {
        key: id
      }, /*#__PURE__*/React.createElement("td", null, firstName), /*#__PURE__*/React.createElement("td", null, secondName), /*#__PURE__*/React.createElement("td", null, phoneNum), /*#__PURE__*/React.createElement("td", null, nick), /*#__PURE__*/React.createElement("td", null), /*#__PURE__*/React.createElement("td", null, accesableYears), /*#__PURE__*/React.createElement("td", null, type), /*#__PURE__*/React.createElement("td", null, avatar), /*#__PURE__*/React.createElement("td", null, admin), /*#__PURE__*/React.createElement("td", null, hidden));
    }))), /*#__PURE__*/React.createElement("input", {
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
setTimeout(() => {
  showDialog( /*#__PURE__*/React.createElement(DialogInstructorsManage, null));
}, 1000);
//# sourceMappingURL=dialog_instructors_manage.js.map