import React, {Component} from "react";

class PingPong extends Component {
    state = {
        text: ""
    }

    componentDidMount() {
        const requestOptions = {
            method: "GET",
        };

        fetch("/resources/javaee8", requestOptions)
            .then((result) => result.text())
            .then((text) => {
                this.setState({text})
            }, (error) => {
                console.log(error)
            })
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