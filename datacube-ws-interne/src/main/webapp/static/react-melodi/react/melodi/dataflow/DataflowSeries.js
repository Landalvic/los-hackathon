import React from "react";
import { connect } from "react-redux";
import DataflowOnglets from "./DataflowOnglets";
import ReactTable from "react-table";
import { waitingTrue, waitingFalse } from "../../commun/GeneralReducer";
import { fetchSources } from "../VisualisationReducer";
import { initGet } from "../../_utils";
import { WS_CONTEXT_PATH } from "../../_properties";
import "react-table/react-table.css";

class DataflowSeries extends React.Component {
  constructor(props) {
    super(props);
    this.state = { panier: [], series: [] };
    this.handleChangePanierSeries = this.handleChangePanierSeries.bind(this);
    this.handleClickSource = this.handleClickSource.bind(this);
  }

  componentDidMount() {
    this.props.fetchSources();
  }

  handleChangePanierSeries(e) {
    const liste = state.panier.filter(iri => iri !== e.target.value);
    if (liste.length === state.panier.length) {
      liste.push(e.target.value);
    }
    this.setState({ panier: liste });
  }

  handleClickSource(source, e) {
    this.props.waitingTrue();
    fetch(WS_CONTEXT_PATH + "/dataflows/dataflow/" + this.props.match.params.dataflow + "/series-potentielles?source=" + source.code, initGet())
      .then(response => response.json())
      .then(json => {
        this.props.waitingFalse();
        this.setState({ series: json });
      })
      .catch(error => {
        this.props.waitingFalse();
        console.log(error);
      });
  }

  render() {
    const sources = this.props.sources.map((source, index) => (
      <li key={index} onClick={e => this.handleClickSource(source, e)} className="li-dataflow">
        {source.libelle}
      </li>
    ));
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
        Cell: props => (
          <div className="center">
            <input type="checkbox" onChange={this.handleChangePanierSeries} />
          </div>
        )
      }
    ];
    return (
      <div>
        <DataflowOnglets dataflow={this.props.match.params.dataflow} active={2} />
        <div className="tab-pane">
          <div className="row no-margin-left margin-bottom">
            <div className="row.collapse">
              <div className="large-2 columns">
                <h3>Sources</h3>
                <ul>{sources}</ul>
              </div>
              <div className="large-10 columns">
                <ReactTable defaultPageSize={10} minRows={0} data={this.state.series} columns={columns} />
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ visualisationReducer }) => {
  return {
    sources: visualisationReducer.sources
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
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DataflowSeries);
