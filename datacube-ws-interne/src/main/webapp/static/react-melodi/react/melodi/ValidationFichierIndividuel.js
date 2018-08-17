import React from "react";
import { connect } from "react-redux";
import ReactTable from "react-table";
import "react-table/react-table.css";
import { FilAriane } from "../commun/FilAriane";
import { initGet } from "../_utils";
import { WS_CONTEXT_PATH } from "../_properties";
import { waitingTrue, waitingFalse } from "../commun/GeneralReducer";
import { setFichiers } from "./VisualisationReducer";
import { changePanierValidationFichier } from "./ChargementReducer";

class ValidationFichierIndividuel extends React.Component {
  constructor(props) {
    super(props);
    this.handleChangeValidation = this.handleChangeValidation.bind(this);
    this.handleClickValider = this.handleClickValider.bind(this);
  }

  componentDidMount() {
    if (this.props.fichiers.length === 0) {
      this.props.waitingTrue();
      fetch(WS_CONTEXT_PATH + "/fichiers", initGet())
        .then(response => response.json())
        .then(json => {
          this.props.waitingFalse();
          this.props.setFichiers(json);
        })
        .catch(error => {
          this.props.waitingFalse();
          console.log(error);
        });
    }
  }

  handleChangeValidation(e) {
    this.props.changePanierValidationFichier(e.target.value);
  }

  handleClickValider(e) {
    this.props.waitingTrue();
    fetch(WS_CONTEXT_PATH + "/fichiers/valider", initPost(this.props.panierValidationFichier))
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
        Header: "Fichier",
        accessor: "code"
      },
      {
        Header: "Etat",
        accessor: "etat",
        Cell: props => <div className="center">{props.value}</div>
      },
      {
        Header: "Date chargement",
        accessor: "datecreation",
        Cell: props => <div className="center">{new Intl.DateTimeFormat("fr-FR").format(props.value)}</div>
      },
      {
        Header: "Taille fichier",
        accessor: "taille",
        Cell: props => <div className="center">{props.value} o</div>
      },
      {
        Header: "Téléchargement",
        accessor: "code",
        Cell: props => (
          <div className="center telecharger">
            <a href={WS_CONTEXT_PATH + "/fichier/" + props.value}>
              <img className="icone" src="/static/icons/download.svg" alt="Télécharger l'archive" />
            </a>
          </div>
        )
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
      { libelle: "Valider des fichiers" }
    ];
    return (
      <div>
        <FilAriane filAriane={filAriane} titre="Valider des fichiers" />
        <div className="large-12 large-centered columns">
          <div className="large-2 columns" />
          <div className="large-10 columns">
            <ReactTable defaultPageSize={10} minRows={0} data={this.props.fichiers} columns={columns} />
          </div>
        </div>
        {this.props.panierValidationFichier.length > 0 ? (
          <div className="large-12 large-centered columns">
            <div className="large-12 columns">
              <div className="right">
                <input
                  className="button boutonFichier"
                  type="button"
                  value={"Valider (" + this.props.panierValidationFichier.length + ")"}
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
    fichiers: visualisationReducer.fichiers,
    panierValidationFichier: chargementReducer.panierValidationFichier
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
    setFichiers: fichiers => {
      dispatch(setFichiers(fichiers));
    },
    changePanierValidationFichier: iri => {
      dispatch(changePanierValidationFichier(iri));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ValidationFichierIndividuel);
