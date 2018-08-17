import "babel-polyfill";
import "isomorphic-fetch";
import { Promise } from "es6-promise";
import thunkMiddleware from "redux-thunk";
import { applyMiddleware, createStore, compose } from "redux";
import { BrowserRouter } from "react-router-dom";
import ReactDOM from "react-dom";
import React from "react";
import { Provider } from "react-redux";
import Main from "./commun/Main";
import combinedReducer from "./combined-reducer";

Promise.polyfill();

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
const store = createStore(combinedReducer, composeEnhancers(applyMiddleware(thunkMiddleware)));

ReactDOM.render(
  <Provider store={store}>
    <BrowserRouter>
      <Main />
    </BrowserRouter>
  </Provider>,
  document.getElementById("application")
);
