const initialState = {
  waiting: 0,
  context: null
};

export const generalReducer = function(state = initialState, action) {
  switch (action.type) {
    case "WAITING_TRUE": {
      return Object.assign({}, state, {
        waiting: state.waiting + 1
      });
    }
    case "WAITING_FALSE": {
      return Object.assign({}, state, {
        waiting: state.waiting - 1
      });
    }
    case "SET_CONTEXT": {
      return Object.assign({}, state, {
        context: action.context
      });
    }
    default:
      return state;
  }
};

export const setContext = context => {
  return {
    type: "SET_CONTEXT",
    context
  };
};

export const waitingTrue = () => {
  return {
    type: "WAITING_TRUE"
  };
};

export const waitingFalse = () => {
  return {
    type: "WAITING_FALSE"
  };
};
