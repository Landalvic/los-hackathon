import React from "react";
import { connect } from "react-redux";
import ReactTable from "react-table";
import "react-table/react-table.css";
import { FilAriane } from "../../commun/FilAriane";
import { initGet, telechargerFichier } from "../../_utils";
import { WS_CONTEXT_PATH } from "../../_properties";
import { waitingTrue, waitingFalse } from "../../commun/GeneralReducer";
import { Link } from "react-router-dom";

class CodeListeVisualiser extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      conceptScheme: null
    };
  }

  componentDidMount() {
    this.props.waitingTrue();
    fetch(WS_CONTEXT_PATH + "/code-liste/" + this.props.match.params.codeListe, initGet())
      .then(response => response.json())
      .then(json => {
        this.props.waitingFalse();
        this.setState({ conceptScheme: json });
      })
      .catch(error => {
        console.log(error);
      });
  }

  onClickTelecharger = () => {
    fetch(WS_CONTEXT_PATH + "/code-liste/" + this.state.conceptScheme.code + "/telecharger", initGet())
      .then(response => response.text())
      .then(text => {
        this.props.waitingFalse();
        telechargerFichier(text, this.state.conceptScheme.code + ".ttl");
      })
      .catch(error => {
        console.log(error);
      });
  };

  render() {
    const { context } = this.props;
    const columnsModalites = [
      {
        Header: "Code",
        accessor: "code"
      },
      {
        Header: "Libellé Fr",
        accessor: "libelleFr",
        Cell: props => <div className="center">{props.value}</div>
      },
      {
        Header: "Libellé En",
        accessor: "libelleEn",
        Cell: props => <div className="center">{props.value}</div>
      }
    ];
    const columnsDatacubes = [
      {
        Header: "Code",
        accessor: "code"
      },
      {
        Header: "Libellé",
        accessor: "libelle",
        Cell: props => <div className="center">{props.value}</div>
      }
    ];
    const filAriane = [
      {
        href: context + "/los/react/melodi",
        libelle: "MELODI"
      },
      {
        href: context + "/los/react/melodi/code-liste/rechercher",
        libelle: "Visualiser les listes de code"
      },
      { libelle: "Visualiser une liste de code" }
    ];
    return (
      <div>
        <FilAriane filAriane={filAriane} titre="Visualiser une liste de code" />
        <div className="large-12 large-centered columns">
          {this.state.conceptScheme ? (
            <div>
              <div className="row margin-bottom">
                <div className="large-7 columns">
                  <p>Code : {this.state.conceptScheme.code}</p>
                  <p>Libellé Fr : {this.state.conceptScheme.libelleFr}</p>
                  <p>Libellé En : {this.state.conceptScheme.libelleEn}</p>
                  <ReactTable defaultPageSize={10} minRows={0} data={this.state.conceptScheme.modalites} columns={columnsModalites} />
                </div>
                <div className="large-5 columns">
                  <p>DSD utilisant la liste de code :</p>
                  <ReactTable defaultPageSize={10} minRows={0} data={this.state.conceptScheme.datacubes} columns={columnsDatacubes} />
                </div>
              </div>
              <div className="row">
                <div className="large-7 columns" />
                <div className="large-5 columns">
                  <input type="submit" value="Télécharger Turtle" onClick={this.onClickTelecharger} />
                </div>
              </div>
            </div>
          ) : null}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ generalReducer }) => {
  return {
    context: generalReducer.context
  };
};

const mapDispatchToProps = dispatch => {
  return {
    waitingTrue: () => {
      dispatch(waitingTrue());
    },
    waitingFalse: () => {
      dispatch(waitingFalse());
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CodeListeVisualiser);
