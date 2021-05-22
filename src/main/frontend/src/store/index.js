import createStore from "redux-light";

export const loadState = () => {
  try {
    const serializedState = localStorage.getItem("state");
    if (serializedState === null) {
      return undefined;
    }
    return JSON.parse(serializedState);
  } catch (err) {
    return undefined;
  }
};

let state = loadState();
if (state === undefined) {
  state = {
    authentication: {
      token: null,
    },
  };
}

const store = createStore(state);

// for debug purposes only
if (process.env.NODE_ENV !== "production") {
  store.subscribe((prevState, state, changes) => {
    let { type, ...otherProps } = changes;
    console.log(type, otherProps);
  });
}

export const saveState = (state) => {
  try {
    const serializedState = JSON.stringify(state);
    localStorage.setItem("state", serializedState);
  } catch {
    // ignore write errors
  }
};

store.subscribe((prevState, state, changes) => {
  saveState(state);
});

export const getState = store.getState;
export const setState = store.setState;
export const resetState = store.resetState;
export default store;
