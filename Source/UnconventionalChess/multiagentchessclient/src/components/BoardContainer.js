import React, {Component} from "react";

import WithMoveValidation from "../integrations/WithMoveValidation";

class BoardContainer extends Component {
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
