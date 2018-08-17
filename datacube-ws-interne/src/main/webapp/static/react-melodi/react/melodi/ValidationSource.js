import React from "react";
import { connect } from "react-redux";
import ReactTable from "react-table";
import "react-table/react-table.css";
import { FilAriane } from "../commun/FilAriane";
import { initGet } from "../_utils";
import { WS_CONTEXT_PATH } from "../_properties";
import { waitingTrue, waitingFalse } from "../commun/GeneralReducer";
import { fetchSources } from "./VisualisationReducer";
import { changePanierValidationSource } from "./ChargementReducer";

class ValidationSource extends React.Component {
  constructor(props) {
    super(props);
    this.handleChangeValidation = this.handleChangeValidation.bind(this);
    this.handleClickValider = this.handleClickValider.bind(this);
  }

  componentDidMount() {
    this.props.fetchSources();
  }

  handleChangeValidation(e) {
    this.props.changePanierValidationSource(e.target.value);
  }

  handleClickValider(e) {
    this.props.waitingTrue();
    fetch(WS_CONTEXT_PATH + "/sources/valider", initPost(this.props.panierValidationSource))
      .then(response => response.json())
      .then(json => {
        this.props.waitingFalse();
      })
      .catch(error => {
        this.props.waitingFalse();
        console.log(error);
      });
  }

  render() {
    const columns = [
      {
        Header: "Source",
        accessor: "libelle"
      },
      {
        Header: "Etat",
        accessor: "etat",
        Cell: props => <div className="center">{props.value}</div>
      },
      {
        Header: "Validation",
        accessor: "iri",
        Cell: props => (
          <div className="center">
            <input type="checkbox" value={props.value} onChange={this.handleChangeValidation} />
          </div>
        )
      }
    ];
    const filAriane = [
      {
        href: "/los/react/melodi",
        libelle: "MELODI"
      },
      { libelle: "Valider des sources" }
    ];
    return (
      <div>
        <FilAriane filAriane={filAriane} titre="Valider des sources" />
        <div className="large-12 large-centered columns">
          <div className="large-2 columns" />
          <div className="large-10 columns">
            <ReactTable defaultPageSize={10} minRows={0} data={this.props.sources} columns={columns} />
          </div>
        </div>
        {this.props.panierValidationSource.length > 0 ? (
          <div className="large-12 large-centered columns">
            <div className="large-12 columns">
              <div className="right">
                <input
                  className="button boutonFichier"
                  type="button"
                  value={"Valider (" + this.props.panierValidationSource.length + ")"}
                  onClick={this.handleClickValider}
                />
              </div>
            </div>
          </div>
        ) : null}
      </div>
    );
  }
}

const mapStateToProps = ({ visualisationReducer, chargementReducer }) => {
  return {
    sources: visualisationReducer.sources,
    panierValidationSource: chargementReducer.panierValidationSource
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
    fetchSources: () => {
      dispatch(fetchSources());
    },
    changePanierValidationSource: iri => {
      dispatch(changePanierValidationSource(iri));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ValidationSource);
