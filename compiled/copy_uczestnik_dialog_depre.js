import { nitro } from "./main.js";
import { jsx as ___EmotionJSX } from "@emotion/react";
const {
  useEffect,
  useState,
  createRef,
  useCallback
} = React;
export default class CopyUczestnikLegacyDialog extends React.Component {
  constructor({
    uczestnikId,
    dialog
  }) {
    super(undefined); //counterDecreaser,globalMessageCounterUpdater

    this.uczestnikId = uczestnikId;
    this.dialog = dialog;
    dialog.setTitle( /*#__PURE__*/React.createElement("span", null, "Kopiowanie uczestnika"));
    this.state = {
      uczestnik: null,
      foundPlaces: null,
      error: null,
      seasons: null,
      selectedSeasonId: -1
    };
    this.loadDataFromServer();
    this.findMiejsceInput = createRef();
  }
  loadDataFromServer() {
    nitro(`uczestnicy/${this.uczestnikId}/with_miejsce`, this, 'uczestnik');
    nitro(`seasons`, this, 'seasons');
  }
  componentWillUnmount() {}
  componentDidMount() {}
  componentDidUpdate(prevProps, prevState, _prevContext) {}
  render() {
    if (this.state.error != null) {
      return /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("span", null, "Wyst\u0105pi\u0142 b\u0142\u0105d!"), /*#__PURE__*/React.createElement("span", null, this.state.error));
    }
    if (this.state.uczestnik == null) {
      return /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("span", null, "wczytywanie [ucz]..."));
    }
    if (this.state.seasons == null) {
      return /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("span", null, "wczytywanie [sezony]..."));
    }
    if (this.state.selectedSeasonId == -1) {
      this.state.selectedSeasonId = this.state.seasons[this.state.seasons.length - 1]['id'];
    }

    //LATA/SEZONY
    const seasons = this.state.seasons;
    const seasons_els = seasons.map((s, i) => {
      const {
        id,
        nazwa,
        timestamp,
        odStr,
        doStr
      } = s;
      return /*#__PURE__*/React.createElement("span", {
        onClick: () => {
          this.setState({
            selectedSeasonId: id,
            foundPlaces: null
          });
        },
        key: i,
        className: this.state.selectedSeasonId == id ? "selected" : null
      }, nazwa);
    });

    //UCZESTNICY-WYSZUKANI
    const uczestnik = this.state.uczestnik;
    const {
      imie,
      nazwisko,
      imieOpiekun,
      nazwiskoOpiekun,
      miejsce,
      grupa
    } = uczestnik;
    const fp = this.state.foundPlaces;
    const places_els = [];
    for (let i = 0; i < 10; i++) {
      if (fp == null || i >= fp.length) {
        places_els.push( /*#__PURE__*/React.createElement("div", {
          key: i
        }, /*#__PURE__*/React.createElement("span", null)));
        continue;
      }
      const {
        id,
        nazwa
      } = fp[i];
      places_els.push( /*#__PURE__*/React.createElement("div", {
        key: i,
        onClick: this.placeSelectorButtonClicked.bind(this, id)
      }, /*#__PURE__*/React.createElement("span", null, nazwa)));
    }
    const result_cont = fp == null ? null : /*#__PURE__*/React.createElement("div", {
      className: "results"
    }, places_els);
    return /*#__PURE__*/React.createElement("div", {
      className: "copy_user_dialog"
    }, /*#__PURE__*/React.createElement("span", {
      className: "user_name"
    }, imie, " ", nazwisko), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
      className: "select_year_side"
    }, /*#__PURE__*/React.createElement("span", null, "docelowy sezon"), /*#__PURE__*/React.createElement("div", null, seasons_els)), /*#__PURE__*/React.createElement("div", {
      className: "search_side"
    }, /*#__PURE__*/React.createElement("span", null, "docelowe miejsce"), /*#__PURE__*/React.createElement("div", {
      className: "search_for_place"
    }, /*#__PURE__*/React.createElement("input", {
      ref: this.findMiejsceInput,
      type: "text",
      onChange: this.searchForMiejsce.bind(this),
      placeholder: "Szukaj miejsca..."
    }), result_cont))));
  }
  placeSelectorButtonClicked(id) {
    /*fet(`miejsca/${id}/with_groups`).then(res=>{
        this.setState({selectedPlaceData: res})
    }).catch(err=>{
        this.setState({error: err})
    })*/
    this.dialog.setClosed(true);
    /*zmienAktywneMiejsce(id,()=>{//zmienia miejsce
        dodajUczestnika(()=>{//otwiera dialog
            let ucz = this.state.uczestnik
              fillAddUserFormWithData(ucz);//wypełnia dialog danymi
              //przesyłane do PHP aby po zapisaniu nowego uczestnika zmienić kolor starego
            //przesłane id zostaje dodatkowo zapisane w nowym uczestniku w bazie jako "copiedFrom"
            const i = (id)=>{
                return document.getElementById(id)
            }
            i("formularzDodawaniaUzytkownika2PrzepisywanyUczestnikId").value = this.uczestnikId
              i("formularzDodawaniaUzytkownika2Dodanie").value = new Date().toJSON().slice(0,10)
            i("formularzDodawaniaUzytkownika2WiekDziecka").value = parseInt(i("formularzDodawaniaUzytkownika2WiekDziecka").value)+1
        },id)
    })*/
    setActiveSeason(this.state.selectedSeasonId, () => {
      zmienAktywneMiejsce(id, () => {
        dodajUczestnika(() => {
          //otwiera dialog
          let ucz = this.state.uczestnik;
          fillAddUserFormWithData(ucz); //wypełnia dialog danymi

          //przesyłane do PHP aby po zapisaniu nowego uczestnika zmienić kolor starego
          //przesłane id zostaje dodatkowo zapisane w nowym uczestniku w bazie jako "copiedFrom"
          const i = id => {
            return document.getElementById(id);
          };
          i("formularzDodawaniaUzytkownika2PrzepisywanyUczestnikId").value = this.uczestnikId;
          i("formularzDodawaniaUzytkownika2Dodanie").value = new Date().toJSON().slice(0, 10);
          i("formularzDodawaniaUzytkownika2WiekDziecka").value = parseInt(i("formularzDodawaniaUzytkownika2WiekDziecka").value) + 1;
        }, id);
      });
    });
  }
  searchForMiejsce() {
    //setTimeout(()=>{
    const str = this.findMiejsceInput.current.value;
    if (str.length < 1) {
      return;
    }
    fet(`miejsca/search`, {
      method: "POST",
      body: JSON.stringify({
        name: str,
        season: this.state.selectedSeasonId
      })
    }).then(res => {
      this.setState({
        foundPlaces: res
      });
    }).catch(err => {
      this.setState({
        error: err.error
      });
    });
    //},1)
  }
}
//# sourceMappingURL=copy_uczestnik_dialog_depre.js.map