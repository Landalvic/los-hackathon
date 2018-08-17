import React from "react";
import { connect } from "react-redux";
import ReactTable from "react-table";
import "react-table/react-table.css";
import { FilAriane } from "../../commun/FilAriane";
import { initGet, initDelete } from "../../_utils";
import { WS_CONTEXT_PATH } from "../../_properties";
import { waitingTrue, waitingFalse } from "../../commun/GeneralReducer";
import { setListeDSD } from "../VisualisationReducer";
import { Link } from "react-router-dom";

class DSDRechercher extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    if (this.props.listeDSD.length == 0) {
      this.props.waitingTrue();
      fetch(WS_CONTEXT_PATH + "/dsd", initGet())
        .then(response => response.json())
        .then(json => {
          this.props.waitingFalse();
          this.props.setListeDSD(json);
        })
        .catch(error => {
          console.log(error);
        });
    }
  }

  onClickSupprimer = event => {
    this.props.waitingTrue();
    const name = event.target.name;
    fetch(WS_CONTEXT_PATH + "/dsd/" + name + "/suppression", initDelete())
      .then(response => {
        const liste = this.props.listeDSD.filter(dsd => dsd.code !== name);
        this.props.setListeDSD(liste);
        this.props.waitingFalse();
      })
      .catch(error => {
        console.log(error);
      });
  };

  render() {
    const { context, listeDSD } = this.props;
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
            <Link to={context + "/los/react/melodi/dsd/chargement/generalites/" + props.value}>
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
      { libelle: "Visualiser les DSD" }
    ];
    return (
      <div>
        <FilAriane filAriane={filAriane} titre="Visualiser les DSD" />
        <div className="large-12 large-centered columns">
          <div className="row.collapse">
            <div className="large-2 columns" />
            <div className="large-10 columns">
              <ReactTable defaultPageSize={10} minRows={0} data={listeDSD} columns={columns} />
            </div>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ generalReducer, visualisationReducer }) => {
  return {
    context: generalReducer.context,
    listeDSD: visualisationReducer.listeDSD
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
    setListeDSD: listeDSD => {
      dispatch(setListeDSD(listeDSD));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DSDRechercher);
