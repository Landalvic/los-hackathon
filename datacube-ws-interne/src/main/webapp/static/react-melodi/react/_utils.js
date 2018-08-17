export const initPost = params => {
  const header = new Headers();
  header.append("Content-Type", "application/json; charset=utf-8");
  const init = {
    //credentials: "include",
    method: "POST",
    headers: header,
    body: JSON.stringify(params)
  };

  return init;
};

export const initPostFile = file => {
  const data = new FormData();
  data.append("file", file);
  const init = {
    //credentials: "include",
    method: "POST",
    body: data
  };

  return init;
};

export const reduxReferentiel = (liste, id) => {
  const byId = {};
  liste.forEach(objet => (byId[objet[id]] = objet));
  return {
    byId: byId,
    allIds: liste.map(objet => objet[id])
  };
};

export const handleChangeState = thiz => {
  return function(event) {
    const target = event.target;
    const value = target.type === "checkbox" ? target.checked : target.value;
    const name = target.name;

    thiz.setState({
      [name]: value
    });
  };
};

export const handleChangeRedux = (event, fonction, object, objectReturn) => {
  const target = event.target;
  const value = target.type === "checkbox" ? target.checked : target.value;
  const name = target.name;

  if (object) {
    object[name] = value;
    if (objectReturn) {
      fonction(objectReturn);
    } else {
      fonction(object);
    }
  } else {
    fonction(value);
  }
};

export const getElementHeight = e => {
  if (e) {
    return e.scrollHeight;
  } else return 0;
};

export const getElementWidth = e => {
  if (e) {
    return e.scrollWidth;
  } else return 0;
};

export const initGet = () => {
  const header = new Headers();
  header.append("Content-Type", "application/json; charset=utf-8");
  const init = {
    //credentials: "include",
    method: "GET",
    headers: header
  };
  return init;
};

export const initDelete = () => {
  const header = new Headers();
  header.append("Content-Type", "application/json; charset=utf-8");
  const init = {
    //credentials: "include",
    method: "DELETE",
    headers: header
  };
  return init;
};

export const telechargerFichier = (data, fileName) => {
  const url = window.URL.createObjectURL(new Blob([data]));
  const link = document.createElement("a");
  link.href = url;
  link.setAttribute("download", fileName);
  document.body.appendChild(link);
  link.click();
};
