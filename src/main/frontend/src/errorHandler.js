import {useHistory} from "react-router";

const handleError = (e) => {
    const history = useHistory();
    let msg;
    if (e.response === undefined) {
        msg = "Ups";
    } else {
        if (e.response.status !== undefined
            && (e.response.status === 403 || e.response.status === 401)) {
            history.push("/errors/forbidden");
            return;
        }
        if (e.response.status === 500 && e.response.data.message === "Ups") {
            history.push("/errors/internal");
            return;
        }
        msg = e.response.data.message;
    }
    throw new Error(msg);
};

export default handleError;