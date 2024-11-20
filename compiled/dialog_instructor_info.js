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
import * as styles from "../../css-modules/dialog_instructor_info.css";

//import { jsx } from '@emotion/react'
import { jsx as ___EmotionJSX } from "@emotion/react";
export class DialogInstructorInfo extends React.Component {
  instructorId = -1;
  state = {
    instructor: null
  };
  constructor(props) {
    super(props);
    this.instructorId = props.instructorId;
    const reactEl = this;
    const errorF = err => {
      console.log(err);
      this.setState({
        ...this.state,
        error: err
      });
    };
    fet(`instructors/${this.instructorId}`).then(res => {
      this.setState({
        ...this.state,
        instructor: res
      });

      //this.dialog.current.setTitle(`instruktor [${res?.firstName ?? "--"} ${res?.lastName ?? "--"}]`)
    }).catch(errorF);
  }
  dialog = createRef();
  render() {
    if (this.state.error != null) {
      return /*#__PURE__*/React.createElement(Dialog, {
        ref: this.dialog,
        title: `instruktor [ERR]`
      }, /*#__PURE__*/React.createElement("span", null, this.state.error.text()));
    }
    const instructor = this.state.instructor;
    return /*#__PURE__*/React.createElement(Dialog, {
      ref: this.dialog,
      title: `instruktor [${instructor?.firstName ?? "--"} ${instructor?.lastName ?? "--"}]`
    }, instructor == null ? /*#__PURE__*/React.createElement("span", null, "czekaj...") : /*#__PURE__*/React.createElement("div", {
      className: styles.dialog_instructor_info
    }, /*#__PURE__*/React.createElement("table", null, /*#__PURE__*/React.createElement("tbody", null, /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("th", null, "imie"), /*#__PURE__*/React.createElement("th", null, "nazwisko"), /*#__PURE__*/React.createElement("th", null, "telefon"), /*#__PURE__*/React.createElement("th", null, "login"), /*#__PURE__*/React.createElement("th", null, "rodzaj")), /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("td", null, instructor.firstName), /*#__PURE__*/React.createElement("td", null, instructor.lastName), /*#__PURE__*/React.createElement("td", null, instructor.phoneNum), /*#__PURE__*/React.createElement("td", null, instructor.nick), /*#__PURE__*/React.createElement("td", null, instTypeToStr(instructor.type))))), /*#__PURE__*/React.createElement(DivInstructorAdditionalInfo, {
      instructor: instructor
    })));
  }
}
function chainClasses() {
  let ret = "";
  for (var i = 0; i < arguments.length; i++) {
    ret += arguments[i];
  }
}
function DivInstructorAdditionalInfo(props) {
  const instructor = props.instructor;
  const additionalInfo = instructor.additionalInfo;

  //css={{backgroundColor: 'hotpink'}}

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
        fet(`instructors/${instructor.id}`, {
          method: "PATCH",
          body: {
            id: instructor.id,
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
    return "student";
  } else if (typeInt == 1) {
    return "firma";
  } else if (typeInt == 2) {
    return "pracownik";
  } else {
    return "inny";
  }
}
//# sourceMappingURL=dialog_instructor_info.js.map