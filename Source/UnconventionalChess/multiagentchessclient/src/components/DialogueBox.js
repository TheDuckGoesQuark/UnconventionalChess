import React, {Component} from "react";
import PropTypes from "prop-types";
import {Message} from "../commons/Chat";

class DialogueBox extends Component {
    static propTypes = {
        message: PropTypes.instanceOf(Message)
    };

    noMessage = () => {
        return (<div>
            <h3>Dialogue Box</h3>
            <p>...</p>
        </div>)
    };

    showMessage = (message) => {
        const {fromId, messageBody} = message;

        return (<div>
            <h3>{fromId}</h3>
            <p>{messageBody}</p>
        </div>)
    };

    render() {
        const {message} = this.props;
        return message ? this.showMessage(message) : this.noMessage();
    }
}

export default DialogueBox;