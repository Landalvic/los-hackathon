import React from "react";
import { connect } from "react-redux";
import ReactTable from "react-table";
import "react-table/react-table.css";
import { FilAriane } from "../../commun/FilAriane";
import { initGet, initDelete } from "../../_utils";
import { WS_CONTEXT_PATH } from "../../_properties";
import { waitingTrue, waitingFalse } from "../../commun/GeneralReducer";
import { fetchConceptSchemes } from "../ReferentielReducer";
import { Link } from "react-router-dom";

class CodeListeRechercher extends React.Component {
  constructor(props) {
    super(props);
    this.state = { codeErreur: undefined };
  }

  componentDidMount() {
    this.props.fetchConceptSchemes();
  }

  onClickSupprimer = event => {
    this.props.waitingTrue();
    const name = event.target.name;
    fetch(WS_CONTEXT_PATH + "/code-liste/" + name + "/suppression", initDelete())
      .then(response => {
        if (response.ok) {
          const liste = this.props.conceptSchemes.filter(cs => cs.code !== name);
          this.props.setConceptSchemes(liste);
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
    const { context } = this.props;
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
        accessor: "code",
        Cell: props => (
          <div className="center">
            <Link to={context + "/los/react/melodi/code-liste/" + props.value}>
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
      { libelle: "Visualiser les listes de code" }
    ];
    return (
      <div>
        <FilAriane filAriane={filAriane} titre="Visualiser les listes de code" />
        <div className="large-12 large-centered columns">
          <div className="row.collapse">
            <div className="large-2 columns" />
            <div className="large-10 columns">
              <ReactTable defaultPageSize={10} minRows={0} data={this.props.conceptSchemes} columns={columns} />
              {this.state.codeErreur === 403 ? (
                <div className="gras red-text margin-top">Ce code liste est déjà utilisé par une DSD, vous ne pouvez pas le supprimer</div>
              ) : null}
            </div>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ generalReducer, referentielReducer }) => {
  return {
    context: generalReducer.context,
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
    fetchConceptSchemes: () => {
      dispatch(fetchConceptSchemes());
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CodeListeRechercher);
