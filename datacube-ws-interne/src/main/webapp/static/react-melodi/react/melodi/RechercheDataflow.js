import React from "react";
import { connect } from "react-redux";
import ReactTable from "react-table";
import "react-table/react-table.css";
import { FilAriane } from "../commun/FilAriane";
import { initGet } from "../_utils";
import { WS_CONTEXT_PATH } from "../_properties";
import { waitingTrue, waitingFalse } from "../commun/GeneralReducer";
import { setDataflows } from "./VisualisationReducer";
import { Link } from "react-router-dom";

class RechercheDataflow extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    if (this.props.dataflows.length == 0) {
      this.props.waitingTrue();
      fetch(WS_CONTEXT_PATH + "/dataflows", initGet())
        .then(response => response.json())
        .then(json => {
          this.props.waitingFalse();
          this.props.setDataflows(json);
        })
        .catch(error => {
          console.log(error);
        });
    }
  }

  render() {
    const columns = [
      {
        Header: "Code",
        accessor: "code"
      },
      {
        Header: "Libelle",
        accessor: "libelle",
        Cell: props => <div className="center">{props.value}</div>
      },
      {
        Header: "Validation",
        accessor: "code",
        Cell: props => (
          <div className="center">
            <Link to={"/los/react/melodi/dataflow/" + props.value}>
              <img className="icone" src="/static/icons/pencil.svg" alt="Modifier" />
            </Link>
          </div>
        )
      }
    ];
    const filAriane = [
      {
        href: "/los/react/melodi",
        libelle: "MELODI"
      },
      { libelle: "Recherche des dataflows" }
    ];
    return (
      <div>
        <FilAriane filAriane={filAriane} titre="Rechercher des dataflows" />
        <div className="large-12 large-centered columns">
          <div className="row.collapse text-right margin-bottom">
            <img className="icone" src="/static/icons/page-add.svg" alt="Ajouter un dataflow" />
            <Link className="lien-action" to="/los/react/melodi/dataflow/">
              Ajouter un dataflow
            </Link>
          </div>
          <div className="row.collapse">
            <div className="large-2 columns" />
            <div className="large-10 columns">
              <ReactTable defaultPageSize={10} minRows={0} data={this.props.dataflows} columns={columns} />
            </div>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ visualisationReducer }) => {
  return {
    dataflows: visualisationReducer.dataflows
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
    setDataflows: dataflows => {
      dispatch(setDataflows(dataflows));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RechercheDataflow);
