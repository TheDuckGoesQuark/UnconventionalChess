import React, {Component} from "react";
import PropTypes from "prop-types";

import WithMoveValidation from "../../integrations/WithMoveValidation";
import InitialConfiguration from "../../commons/InitialConfiguration";

class BoardContainer extends Component {
    static propTypes = {
        onMove: PropTypes.func,
        initialConfig: PropTypes.instanceOf(InitialConfiguration)
    };

    render() {
        const {initialConfig} = this.props;

        return (
            <div style={boardsContainer}>
                <WithMoveValidation initialConfig={initialConfig}/>
            </div>
        )
    }
}

const boardsContainer = {
    display: "flex",
    justifyContent: "space-around",
    alignItems: "center",
    flexWrap: "wrap",
    width: "100%",
    height: "100%",
};

export default BoardContainer;
