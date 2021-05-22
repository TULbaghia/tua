import { api } from "../Api";
import { getState, setState } from "../store";

export function getToken(){
    return getState().authentication.token;
}


export async function signIn(login, password) {
  if (getState().authentication.loading) return;

  setState({
    type: "SIGN_IN_STARTED",
    authentication: { loading: true },
  });

  let token = null;
  try {
    token = await api.login({
        login: login,
        password: password
    });
  } catch (error) {
    setState({
      type: "SIGN_IN_ERROR",
      authentication: {
        loading: false,
        error,
      },
    });
    return;
  }

  setState({
    type: "SIGN_IN_SUCCESS",
    authentication: {
      loading: false,
      token,
    },
  });
}
