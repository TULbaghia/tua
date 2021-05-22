const { Configuration, DefaultApi } = require("api-client");

const url = "https://localhost:8181";
const configuration = new Configuration({
  basePath: url,
});

export const api = new DefaultApi(configuration);
