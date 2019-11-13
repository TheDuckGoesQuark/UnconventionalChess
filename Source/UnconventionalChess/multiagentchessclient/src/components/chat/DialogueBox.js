import React, {Component} from "react";
import {connect} from "react-redux";

class DialogueBox extends Component {
    noMessage = () => {
        return (<div>
            <h3>Dialogue Box</h3>
            <p>...</p>
        </div>)
    };

    showMessage = (message) => {
        const {sourceSquare, targetSquare} = message;

        return (<div>
            <h3>MOVE</h3>
            <p>{sourceSquare},{targetSquare}</p>
        </div>)
    };

    render() {
        const {message} = this.props;
        return <div style={messageStyle}>
            {message ? this.showMessage(message) : this.noMessage()}
        </div>
    }
}

function mapStateToProps(state) {
    return {
        message: state.chatReducer.timeOrderedMessages[state.chatReducer.timeOrderedMessages.length - 1]
    }
}

const messageStyle = {
    backgroundColor: "grey",
    fontcolor: "white",
};

export default connect(mapStateToProps)(DialogueBox);