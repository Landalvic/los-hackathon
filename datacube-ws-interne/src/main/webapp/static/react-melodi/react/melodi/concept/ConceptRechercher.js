import React from "react";
import { connect } from "react-redux";
import ReactTable from "react-table";
import "react-table/react-table.css";
import { FilAriane } from "../../commun/FilAriane";
import { initDelete } from "../../_utils";
import { WS_CONTEXT_PATH } from "../../_properties";
import { Link } from "react-router-dom";
import { waitingTrue, waitingFalse } from "../../commun/GeneralReducer";
import { fetchConcepts, fetchMesures, setConcepts, setMesures } from "../ReferentielReducer";

class ConceptRechercher extends React.Component {
  constructor(props) {
    super(props);
    this.state = { codeErreur: undefined };
  }

  componentDidMount() {
    this.props.fetchConcepts();
    this.props.fetchMesures();
  }

  onClickSupprimer = event => {
    this.props.waitingTrue();
    const name = event.target.name;
    fetch(WS_CONTEXT_PATH + "/concept/" + name + "/suppression", initDelete())
      .then(response => {
        if (response.ok) {
          const newConcepts = this.props.concepts.filter(c => c.code !== name);
          this.props.setConcepts(newConcepts);
          const newMesures = this.props.mesures.filter(c => c.code !== name);
          this.props.setMesures(newMesures);
        } else {
          this.setState({ codeErreur: response.status });
        }
        this.props.waitingFalse();
      })
      .catch(error => {
        console.log(error);
      });
  };

  render() {
    const { context, allConcepts } = this.props;
    const columns = [
      {
        Header: "Code",
        accessor: "code",
        filterable: true
      },
      {
        Header: "Libellé Fr",
        accessor: "libelleFr",
        filterable: true,
        Cell: props => <div className="center">{props.value}</div>
      },
      {
        Header: "Libellé En",
        accessor: "libelleEn",
        filterable: true,
        Cell: props => <div className="center">{props.value}</div>
      },
      {
        Header: "Mesure",
        accessor: "mesure",
        Cell: props => <div className="center">{props.value ? <img className="icone" src={context + "/static/icons/check.svg"} alt="Visualiser" /> : null}</div>
      },
      {
        accessor: "code",
        Cell: props => (
          <div className="center">
            <Link to={context + "/los/react/melodi/concept/" + props.value}>
              <img className="icone" src={context + "/static/icons/page-search.svg"} alt="Visualiser" />
            </Link>
          </div>
        )
      },
      {
        accessor: "code",
        Cell: props => (
          <div className="center">
            <img onClick={this.onClickSupprimer} name={props.value} className="icone" src={context + "/static/icons/trash.svg"} alt="Supprimer" />
          </div>
        )
      }
    ];
    const filAriane = [
      {
        href: context + "/los/react/melodi",
        libelle: "MELODI"
      },
      { libelle: "Visualiser les concepts" }
    ];
    return (
      <div>
        <FilAriane filAriane={filAriane} titre="Visualiser les concepts" />
        <div className="large-12 large-centered columns">
          <div className="row.collapse">
            <div className="large-2 columns" />
            <div className="large-10 columns">
              <ReactTable defaultPageSize={10} minRows={0} data={allConcepts} columns={columns} />
              {this.state.codeErreur === 403 ? (
                <div className="gras red-text margin-top">Ce concept est déjà utilisé par une DSD, vous ne pouvez pas le supprimer</div>
              ) : null}
            </div>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ generalReducer, referentielReducer }) => {
  const concepts = Object.values(referentielReducer.concepts.byId);
  const mesures = Object.values(referentielReducer.mesures.byId);
  return {
    context: generalReducer.context,
    concepts,
    mesures,
    allConcepts: concepts.concat(mesures)
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
    fetchMesures: () => {
      dispatch(fetchMesures());
    },
    setConcepts: concepts => {
      dispatch(setConcepts(concepts));
    },
    setMesures: mesures => {
      dispatch(setMesures(mesures));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ConceptRechercher);
