import React, {Component} from "react";
import PropTypes from "prop-types";

import WithMoveValidation from "../integrations/WithMoveValidation";
import InitialConfiguration from "../commons/InitialConfiguration";

class BoardContainer extends Component {
    static propTypes = {
        onMove: PropTypes.func,
        initialConfig: PropTypes.instanceOf(InitialConfiguration)
    };

    render() {
        return (
            <div style={boardsContainer}>
                <WithMoveValidation/>
            </div>
        )
    }
}

const boardsContainer = {
    display: "flex",
    justifyContent: "space-around",
    alignItems: "center",
    flexWrap: "wrap",
    width: "100vw",
    marginTop: 30,
    marginBottom: 50
};

export default BoardContainer;
