const {
  useEffect,
  useState,
  createRef,
  useCallback,
  cloneElement
} = React;
export function ClassGroupSelectorComp(props) {
  const placeId = props.placeId;
  const selectedPlaceId = props.selectedPlaceId ?? "";
  const onChange = props.onChange ?? null;
  const [groups, setGroups] = useState(null);
  const [value, setValue] = useState(selectedPlaceId);
  useEffect(() => {
    gather(`places/${placeId}/groups`, res => setGroups(res), err => {});
  }, []);
  if (groups == null) {
    return /*#__PURE__*/React.createElement("select", null);
  }
  return /*#__PURE__*/React.createElement("select", {
    value: value,
    onChange: e => {
      setValue(e.target.value);
      onChange?.(parseInt(e.target.value));
    }
  }, /*#__PURE__*/React.createElement("option", {
    value: "0",
    key: 0
  }, "--wybierz--"), groups.map(gr => {
    const {
      id,
      timestamp,
      season,
      name,
      classesStartTime,
      classesEndTime,
      classesDay,
      priceForOneClass,
      sort
    } = gr;
    return /*#__PURE__*/React.createElement("option", {
      key: id,
      value: id
    }, name);
  }));
} //<option value="0" key={0}>wypisany</option>
//# sourceMappingURL=components_common.js.map