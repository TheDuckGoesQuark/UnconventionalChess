import React from 'react'
import {connect} from "react-redux";

const PieceConfig = (props) => {
    return <div>
        <h3>Configuring Piece at {props.configuringSquare}</h3>
        <form>
            <button onClick={props.saveConfig}>Save</button>
        </form>
    </div>
};

function mapStateToProps(state) {
    return {
        configuringSquare: state.configReducer.configuringSquare,
    };
}

const mapDispatchToProps = {
    saveConfig: () => (console.log("Saved config"))
};

export default connect(mapStateToProps, mapDispatchToProps)(PieceConfig)