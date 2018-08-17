import React from "react";
import { connect } from "react-redux";
import { FilAriane } from "../../commun/FilAriane";
import { initPost, initPostFile, handleChangeRedux } from "../../_utils";
import { WS_CONTEXT_PATH } from "../../_properties";
import { waitingTrue, waitingFalse } from "../../commun/GeneralReducer";
import { setDSD } from "../ChargementReducer";
import DSDOnglets from "./DSDOnglets";

class DSDCreer extends React.Component {
  constructor(props) {
    super(props);
  }

  handleChange = event => {
    handleChangeRedux(event, this.props.setDSD, this.props.dsd);
  };

  render() {
    const { dsd } = this.props;
    return (
      <div>
        <DSDOnglets dsdParams={this.props.match.params.dsd} active={1} />
        <div className="tab-pane">
          <div className="row">
            <div className="large-4 columns">
              <label>Code de la DSD :</label>
              <input type="text" name="code" value={dsd.code ? dsd.code : ""} onChange={this.handleChange} />
            </div>
          </div>
          <div className="row">
            <div className="large-4 columns">
              <label>Libellé fr de la DSD :</label>
              <input type="text" name="libelleFr" value={dsd.code ? dsd.libelleFr : ""} onChange={this.handleChange} />
            </div>
          </div>
          <div className="row margin-bottom">
            <div className="large-4 columns">
              <label>Libellé en de la DSD :</label>
              <input type="text" name="libelleEn" value={dsd.code ? dsd.libelleEn : ""} onChange={this.handleChange} />
            </div>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ chargementReducer, referentielReducer }) => {
  return {
    dsd: chargementReducer.dsd,
    concepts: Object.values(referentielReducer.concepts.byId)
  };
};

const mapDispatchToProps = dispatch => {
  return {
    waitingTrue: () => {
      dispatch(waitingTrue());
    },
    waitingFalse: () => {
      dispatch(waitingFalse());
    },
    setDSD: dsd => {
      dispatch(setDSD(dsd));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DSDCreer);
