import {Configuration, DefaultApi} from "ssbd06-api"

const url = `${process.env.REACT_APP_API_BASE_URL}/resources`;
const configuration = new Configuration({
    basePath: url,
});

export const api = new DefaultApi(configuration);

export const buildApi = lang => new DefaultApi({
    ...configuration,
    baseOptions: {
        headers: {
            "Accept-Language": lang
        }
    }
})
