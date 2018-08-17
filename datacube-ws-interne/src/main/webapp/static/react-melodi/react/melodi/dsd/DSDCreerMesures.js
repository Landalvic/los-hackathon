import React from "react";
import { connect } from "react-redux";
import ReactTable from "react-table";
import "react-table/react-table.css";
import { FilAriane } from "../../commun/FilAriane";
import { initPost, initPostFile } from "../../_utils";
import { WS_CONTEXT_PATH } from "../../_properties";
import { waitingTrue, waitingFalse } from "../../commun/GeneralReducer";
import DSDOnglets from "./DSDOnglets";
import FilteredMultiSelect from "react-filtered-multiselect";
import { fetchMesures } from "../ReferentielReducer";
import { setDSD } from "../ChargementReducer";

class DSDCreerMesures extends React.Component {
  constructor(props) {
    super(props);
    this.handleSelectionVariablesChange = this.handleSelectionVariablesChange.bind(this);
    this.handleSelectedVariablesChange = this.handleSelectedVariablesChange.bind(this);
  }

  componentDidMount() {
    this.props.fetchMesures();
  }

  handleSelectionVariablesChange(selectedOptions) {
    this.props.dsd.mesures.push(this.props.mesuresById[selectedOptions[selectedOptions.length - 1].value]);
    this.props.setDSD(this.props.dsd);
  }

  handleSelectedVariablesChange(selectedOptions) {
    this.props.dsd.mesures = this.props.dsd.mesures.filter(mesure => selectedOptions.filter(opt => opt.value === mesure.iri).length == 0);
    this.props.setDSD(this.props.dsd);
  }

  render() {
    const { dsd, mesures } = this.props;
    const mesuresTotales = mesures.map(mesure => ({ value: mesure.iri, text: mesure.code }));
    let mesuresDSD = [];
    if (dsd.mesures) {
      mesuresDSD = dsd.mesures.map(mesure => ({ value: mesure.iri, text: mesure.code }));
    }
    return (
      <div>
        <DSDOnglets dsdParams={this.props.match.params.dsd} active={3} />
        <div className="tab-pane">
          <div className="row margin-bottom">
            <div className="large-12 columns">
              <label>Concepts : </label>
            </div>
          </div>
          <div className="row margin-bottom">
            <div className="large-5 columns">
              <FilteredMultiSelect
                placeholder="Rechercher"
                buttonText="Ajouter"
                onChange={this.handleSelectionVariablesChange}
                options={mesuresTotales}
                selectedOptions={mesuresDSD}
              />
            </div>
            <div className="large-5 columns">
              <FilteredMultiSelect placeholder="Rechercher" buttonText="Enlever" onChange={this.handleSelectedVariablesChange} options={mesuresDSD} />
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
    mesuresById: referentielReducer.mesures.byId,
    mesures: Object.values(referentielReducer.mesures.byId)
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
    fetchMesures: () => {
      dispatch(fetchMesures());
    },
    setDSD: dsd => {
      dispatch(setDSD(dsd));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DSDCreerMesures);
