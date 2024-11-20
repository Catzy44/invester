import { Dialog, showDialog } from "./dialog.js";
import { miesiacF, PlaceType } from "./panel_platnosci.js";
import { gather, zebra } from "./main.js";
import { ClassGroupSelectorComp } from "../compiled/components_common";
const {
  useEffect,
  useState,
  createRef,
  useCallback,
  cloneElement
} = React;
import * as styles from "../../css-modules/dialog_place_info.css";

//import { jsx } from '@emotion/react'
import { jsx as ___EmotionJSX } from "@emotion/react";
export class DialogPlaceInfo extends React.Component {
  placeId = -1;
  state = {
    instructor: null
  };
  constructor(props) {
    super(props);
    this.placeId = props.id;
    const reactEl = this;
    const errorF = err => {
      console.log(err);
      this.setState({
        ...this.state,
        error: err
      });
    };
    fet(`places/${this.placeId}`).then(res => {
      this.setState({
        ...this.state,
        place: res
      });

      //this.dialog.current.setTitle(`instruktor [${res?.firstName ?? "--"} ${res?.lastName ?? "--"}]`)
    }).catch(errorF);
  }
  dialog = createRef();
  render() {
    if (this.state.error != null) {
      return /*#__PURE__*/React.createElement(Dialog, {
        ref: this.dialog,
        title: `miejsce [ERR]`
      }, /*#__PURE__*/React.createElement("span", null, this.state.error.text()));
    }
    const place = this.state.place;
    return /*#__PURE__*/React.createElement(Dialog, {
      ref: this.dialog,
      title: `miejsce [${place?.name ?? "--"}]`
    }, place == null ? /*#__PURE__*/React.createElement("span", null, "czekaj...") : /*#__PURE__*/React.createElement("div", {
      className: styles.dialog_place_info
    }, /*#__PURE__*/React.createElement("table", null, /*#__PURE__*/React.createElement("tbody", null, /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("th", null, "nazwa"), /*#__PURE__*/React.createElement("th", null, "adres"), /*#__PURE__*/React.createElement("th", null, "adres2"), /*#__PURE__*/React.createElement("th", null, "telefon"), /*#__PURE__*/React.createElement("th", null, "rodzaj")), /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null, place.name), /*#__PURE__*/React.createElement("td", null, place.address), /*#__PURE__*/React.createElement("td", null, place.address1), /*#__PURE__*/React.createElement("td", null, place.phoneNum), /*#__PURE__*/React.createElement("td", null, instTypeToStr(place.type))))), /*#__PURE__*/React.createElement(DivPlaceAdditionalInfo, {
      place: place
    })));
  }
}
function DivPlaceAdditionalInfo(props) {
  const place = props.place;
  const additionalInfo = place.additionalInfo;
  const [isProcessing, setProcessing] = useState(false);
  const [isEditing, setEditing] = useState(false);
  const [textareaVal, setTextareaVal] = useState(additionalInfo);
  if (isEditing) {
    return /*#__PURE__*/React.createElement("div", {
      className: `${styles.additional_info_container}${isProcessing ? " " + styles.processing : ""}`
    }, /*#__PURE__*/React.createElement("textarea", {
      onChange: event => {
        setTextareaVal(event.target.value);
      },
      value: textareaVal ?? "--"
    }), /*#__PURE__*/React.createElement("i", {
      onClick: () => {
        if (isProcessing) {
          return;
        }
        setProcessing(true);
        fet(`places/${place.id}`, {
          method: "PATCH",
          body: {
            additionalInfo: textareaVal
          }
        }).then(res => {
          setEditing(false);
          setProcessing(false);
        }).catch(err => {
          setProcessing(false);
        });
      },
      className: "fa-solid fa-floppy-disk"
    }));
  }
  return /*#__PURE__*/React.createElement("div", {
    className: styles.additional_info_container
  }, /*#__PURE__*/React.createElement("textarea", {
    readOnly: true,
    value: textareaVal ?? "--"
  }), /*#__PURE__*/React.createElement("i", {
    onClick: () => {
      setEditing(true);
    },
    className: "fa-solid fa-pen-to-square",
    "aria-hidden": "true"
  }));
}
function instTypeToStr(typeInt) {
  if (typeInt == 0) {
    return "dzieci";
  } else if (typeInt == 1) {
    return "placówka";
  } else if (typeInt == 2) {
    return "dorośli";
  } else {
    return "inny";
  }
}
//# sourceMappingURL=dialog_place_info.js.map