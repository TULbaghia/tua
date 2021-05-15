import React, { Component } from "react";
import { DefaultApi, Configuration } from "api-client"

class PingPong extends Component {
    state = {
        text: ""
    }

    componentDidMount() {
        const requestOptions = {
            method: "GET",
        };


        const conf = new Configuration()
        const api = new DefaultApi(conf)
        api.ping().then((res) => {
            console.log(res)
        })

        // fetch("/resources/javaee8", requestOptions)
        //     .then((result) => result.text())
        //     .then((text) => {
        //         this.setState({text})
        //     }, (error) => {
        //         console.log(error)
        //     })
    }

    render() {
        return (
            <div>
                <center>
                    <h1>{this.state.text}</h1>
                </center>
            </div>
        )
    }
}
export default PingPong;