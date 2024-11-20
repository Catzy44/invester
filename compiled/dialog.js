import WiadomosciUczestnika from "./wiadomosci_dialog.js";
import { jsx as ___EmotionJSX } from "@emotion/react";
const {
  useRef,
  useEffect,
  useState,
  createRef,
  useCallback,
  cloneElement
} = React;
modular.dialogs = {
  showRenderedDialog: dialogObj => {
    const body = document.getElementsByTagName("body")[0];
    const dialogHolder = document.createElement("div");
    body.append(dialogHolder);

    //ReactDOM.createRoot(dialogHolder).render(dialogObj)
    const root = ReactDOM.createRoot(dialogHolder);

    //INJECT REACT ROOT OBJ INTO RENDERED COMPONENT
    const elCl = cloneElement(dialogObj, {
      root: root
    });
    root.render(elCl);
  },
  showDialog: (dialogClass, dialogOpts) => {
    const body = document.getElementsByTagName("body")[0];
    const dialogHolder = document.createElement("div");
    body.append(dialogHolder);
    const dialogEl = React.createElement(dialogClass, dialogOpts);
    ReactDOM.createRoot(dialogHolder).render(dialogEl);
  },
  getByName: dialogName => {
    if (Object.keys(modular.dialogs.dialogs).includes(dialogName)) {
      return modular.dialogs.dialogs[dialogName];
    }
    return null;
  },
  showByName: name => {
    const dialog = modular.dialogs.getByName(name);
    const dialogEl = React.createElement(dialog, null);
    modular.dialogs.showDialog(dialogEl);
  }
};
let dialogRoot = null;
export function showDialog(dialog) {
  /*let body = document.getElementsByTagName("body")[0]
  injectCrEl(body)
  const root = ReactDOM.createRoot(body.crEl("div"))
  root.render(dialog)*/

  /*const body = document.getElementsByTagName("body")[0]
  const dialogHolder = document.createElement("div")
  body.append(dialogHolder)
    ReactDOM.createRoot(dialogHolder).render(dialog)*/
  modular.dialogs.showRenderedDialog(dialog);
}
export class Dialog extends React.Component {
  constructor(props) {
    super(props);
    this.boxEl = createRef();
    this.headerEl = createRef();
    this.titleClickCallback = null;

    /*const element = props.children.type == "div" ? props.children : cloneElement(
        props.children,
        {
            title: (t)=>{this.setState({title:t})},
            dialogCloseHandler: (c)=>{this.setState({closed:c})},
            callbackSetter: (c)=>{this.titleClickCallback = c},
            dialog: this
        }
    )*/

    const element = props.children;
    this.state = {
      wiadomosci: null,
      closed: null,
      content: element,
      title: props.title,
      actualTitle: props.title
    };
  }
  setClosed(b) {
    this.setState({
      closed: b
    });
  }
  setTitle(t) {
    this.setState({
      title: t
    });
  }
  componentDidMount() {
    if (this.boxEl == null || this.boxEl.current == null) {
      return;
    }
    this.makeDraggable(this.boxEl.current, this.headerEl.current);
  }
  render() {
    //this.state.content
    const callback_exists = this.titleClickCallback != null;
    if (this.state.closed) {
      return null;
    }
    const isBlacky = this.props.blacky != null && this.props.blacky;

    //props.title may be null at first
    //but state.title will contain Dialog

    //title in state object has changed
    //propagate title from new state to props
    if (this.state.actualTitle != this.state.title) {
      this.props.title = this.state.title;
      this.state.actualTitle = this.state.title;
    }

    //title in props object has changed
    //propagate new title from props to state
    if (this.state.actualTitle != this.props.title) {
      this.state.title = this.props.title;
      //asdf//
      this.state.actualTitle = this.state.title;
    }
    const title = this.state.actualTitle ?? "Dialog";
    return (
      /*#__PURE__*/
      //<span className={callback_exists ? "click" : ""} onClick={()=>{if(!callback_exists) return; this.titleClickCallback()}}>{this.state.title}</span>
      React.createElement("div", {
        className: `popup_modern${isBlacky ? "_blacky" : ""} centered`,
        ref: this.boxEl
      }, /*#__PURE__*/React.createElement("div", {
        className: "header",
        ref: this.headerEl
      }, isBlacky ? null : /*#__PURE__*/React.createElement("img", {
        src: "https://prestiz.org/images/icon.png"
      }), typeof title === "string" ? /*#__PURE__*/React.createElement("span", null, title) : title, /*#__PURE__*/React.createElement("i", {
        onClick: () => {
          this.setState({
            closed: true
          });
          if (this.props.onClose != null) {
            this.props.onClose();
          }
        },
        className: "fa-solid fa-xmark popup-close-btn",
        style: {
          fontSize: "18px"
        }
      })), /*#__PURE__*/React.createElement("div", {
        className: "content"
      }, this.props.children))
    );
  }
  makeDraggable(elmnt, header) {
    let pos1 = 0,
      pos2 = 0,
      pos3 = 0,
      pos4 = 0;
    if (header != null) {
      // if present, the header is where you move the DIV from:
      header.onmousedown = dragMouseDown;
    } else {
      // otherwise, move the DIV from anywhere inside the DIV:
      elmnt.onmousedown = dragMouseDown;
    }
    function dragMouseDown(e) {
      e = e || window.event;
      if (e.which !== 1) {
        return;
      }
      e.preventDefault();

      // get the mouse cursor position at startup:
      pos3 = e.clientX;
      pos4 = e.clientY;
      document.onmouseup = closeDragElement;
      // call a function whenever the cursor moves:
      document.onmousemove = elementDrag;
    }
    function elementDrag(e) {
      e = e || window.event;
      e.preventDefault();
      // calculate the new cursor position:
      pos1 = pos3 - e.clientX;
      pos2 = pos4 - e.clientY;
      pos3 = e.clientX;
      pos4 = e.clientY;
      // set the element's new position:
      elmnt.style.top = elmnt.offsetTop - pos2 + "px";
      elmnt.style.left = elmnt.offsetLeft - pos1 + "px";
    }
    function closeDragElement() {
      // stop moving when mouse button is released:
      document.onmouseup = null;
      document.onmousemove = null;
    }
  }
}
export function DialogYesNo(props) {
  const dialog = useRef(null);
  return /*#__PURE__*/React.createElement(Dialog, {
    ref: dialog,
    title: props.title != null ? props.title : "potwierdzenie"
  }, /*#__PURE__*/React.createElement("div", {
    className: "dialog_yes_no"
  }, /*#__PURE__*/React.createElement("span", null, "czy na pewno?"), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("input", {
    onClick: () => {
      props.root.unmount();
      props?.yesClicked?.();
    },
    type: "button",
    value: "tak"
  }), /*#__PURE__*/React.createElement("input", {
    onClick: () => {
      props.root.unmount();
      props.noClicked?.();
    },
    type: "button",
    value: "nie"
  }))));
}
//# sourceMappingURL=dialog.js.map