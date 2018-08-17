import React from "react";
import { connect } from "react-redux";
import ReactTable from "react-table";
import "react-table/react-table.css";
import { FilAriane } from "../../commun/FilAriane";
import { initPost, initPostFile, handleChangeRedux } from "../../_utils";
import { WS_CONTEXT_PATH } from "../../_properties";
import { waitingTrue, waitingFalse } from "../../commun/GeneralReducer";
import DSDOnglets from "./DSDOnglets";
import { setDSD } from "../ChargementReducer";
import { fetchConcepts, fetchConceptSchemes } from "../ReferentielReducer";

class DSDCreerSlices extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      indexSliceVisualisation: 0
    };
  }

  componentDidMount() {
    this.props.fetchConcepts();
    this.props.fetchConceptSchemes();
  }

  onClickSupprimer = event => {
    this.props.dsd.slicesKeys = this.props.dsd.slicesKeys.filter(sk => sk.iri !== event.target.name);
    this.props.setDSD(this.props.dsd);
  };

  onClickVisualiser = event => {
    this.setState({ indexSliceVisualisation: event.target.name });
  };

  handleChangeName = event => {
    const { dsd } = this.props;
    const sliceKeySelected = dsd.slicesKeys[this.state.indexSliceVisualisation];
    handleChangeRedux(event, this.props.setDSD, sliceKeySelected, dsd);
  };

  render() {
    const { context, dsd, concepts, conceptSchemes } = this.props;
    let visualisationSliceKey = null;
    const columns = [
      {
        Header: "Slice",
        accessor: "libelleFr"
      },
      {
        accessor: "iri",
        Cell: props => (
          <div className="center">
            <img onClick={this.onClickVisualiser} name={props.index} className="icone" src={context + "/static/icons/page-search.svg"} alt="Visualiser" />
          </div>
        )
      },
      {
        accessor: "iri",
        Cell: props => (
          <div className="center">
            <img onClick={this.onClickSupprimer} name={props.value} className="icone" src={context + "/static/icons/trash.svg"} alt="Supprimer" />
          </div>
        )
      }
    ];
    if (dsd.slicesKeys.length > 0) {
      const sliceKeySelected = dsd.slicesKeys[this.state.indexSliceVisualisation];
      const columnsDimensions = [
        {
          Header: "Concept",
          accessor: "iriConcept",
          Cell: props => <div className="center">{concepts.find(concept => concept.iri === props.value).libelleFr}</div>
        },
        {
          Header: "Code liste",
          accessor: "iriConceptScheme",
          Cell: props => <div className="center">{conceptSchemes.find(concept => concept.iri === props.value).libelleFr}</div>
        }
      ];
      visualisationSliceKey = (
        <div>
          <label>Nom de la slice : </label>
          <input type="text" name="libelleFr" value={sliceKeySelected.libelleFr ? sliceKeySelected.libelleFr : ""} onChange={this.handleChangeName} />
          <label>Selectionner les dimensions à fixer</label>
          <ReactTable defaultPageSize={10} minRows={0} data={dsd.dimensions} columns={columnsDimensions} />
        </div>
      );
    }
    return (
      <div>
        <DSDOnglets dsdParams={this.props.match.params.dsd} active={4} />
        <div className="tab-pane">
          <div className="row margin-bottom">
            <div className="large-6 columns">
              <label>Liste des dimensions : </label>
              <ReactTable defaultPageSize={10} minRows={0} data={dsd.slicesKeys} columns={columns} />
            </div>
            <div className="large-6 columns">{visualisationSliceKey}</div>
          </div>
          <div className="row margin-bottom" />
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ generalReducer, chargementReducer, referentielReducer }) => {
  return {
    context: generalReducer.context,
    dsd: chargementReducer.dsd,
    concepts: Object.values(referentielReducer.concepts.byId),
    conceptSchemes: Object.values(referentielReducer.conceptSchemes.byId)
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
    fetchConcepts: () => {
      dispatch(fetchConcepts());
    },
    fetchConceptSchemes: () => {
      dispatch(fetchConceptSchemes());
    },
    setDSD: dsd => {
      dispatch(setDSD(dsd));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DSDCreerSlices);
