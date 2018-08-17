import React from "react";
import { connect } from "react-redux";
import DataflowOnglets from "./DataflowOnglets";
import { initGet, initPost } from "../../_utils";
import { WS_CONTEXT_PATH } from "../../_properties";
import { waitingTrue, waitingFalse } from "../../commun/GeneralReducer";
import { setVariables, setDataflows } from "../VisualisationReducer";
import FilteredMultiSelect from "react-filtered-multiselect";

class DataflowVisualisation extends React.Component {
  constructor(props) {
    super(props);

    this.state = { iri: null, code: "", libelle: "", selectedOptions: [] };
    this.handleChangeCode = this.handleChangeCode.bind(this);
    this.handleChangeLibelle = this.handleChangeLibelle.bind(this);
    this.handleClickValider = this.handleClickValider.bind(this);
    this.handleSelectionVariablesChange = this.handleSelectionVariablesChange.bind(this);
    this.handleSelectedVariablesChange = this.handleSelectedVariablesChange.bind(this);
  }

  componentDidMount() {
    if (this.props.variables.length == 0) {
      this.props.waitingTrue();
      fetch(WS_CONTEXT_PATH + "/variables", initGet())
        .then(response => response.json())
        .then(json => {
          this.props.waitingFalse();
          this.props.setVariables(json);
        })
        .catch(error => {
          this.props.waitingFalse();
          console.log(error);
        });
    }
    if (this.props.match.params.dataflow) {
      this.props.waitingTrue();
      fetch(WS_CONTEXT_PATH + "/dataflows/dataflow/" + this.props.match.params.dataflow, initGet())
        .then(response => response.json())
        .then(json => {
          this.props.waitingFalse();
          this.setState({ iri: json.iri, code: json.code, libelle: json.libelle, selectedOptions: this.variablesToOptions(json.variables) });
        })
        .catch(error => {
          this.props.waitingFalse();
          console.log(error);
        });
    }
  }

  handleChangeCode(event) {
    this.setState({ code: event.target.value });
  }

  handleChangeLibelle(event) {
    this.setState({ libelle: event.target.value });
  }

  handleSelectionVariablesChange(selectedOptions) {
    selectedOptions.sort((a, b) => a.id - b.id);
    this.setState({ selectedOptions });
  }

  handleSelectedVariablesChange(selectedOptions) {
    const newSelectedOptions = this.state.selectedOptions.filter(option => !selectedOptions.includes(option));
    this.setState({ selectedOptions: newSelectedOptions });
  }

  handleClickValider(event) {
    this.props.waitingTrue();
    const variables = this.state.selectedOptions.map(variable => ({ iri: variable.value }));
    const data = {
      iri: this.state.iri,
      code: this.state.code,
      libelle: this.state.libelle,
      variables: variables
    };
    fetch(WS_CONTEXT_PATH + "/dataflows/dataflow/modifier", initPost(data))
      .then(response => response.json())
      .then(json => {
        this.props.waitingFalse();
        this.props.setDataflows([]);
        this.props.history.push("/los/react/melodi/dataflow/" + this.state.code);
      })
      .catch(error => {
        this.props.waitingFalse();
        console.log(error);
      });
  }

  variablesToOptions(variables) {
    return variables.map(variable => ({ value: variable.iri, text: variable.libelle + " [" + variable.code + "]" }));
  }

  render() {
    const options = this.variablesToOptions(this.props.variables);
    return (
      <div>
        <DataflowOnglets dataflow={this.props.match.params.dataflow} active={1} />
        <div className="tab-pane">
          <div className="row no-margin-left margin-bottom">
            <div className="large-6 columns">
              <label>
                Code <span className="required">*</span> :{" "}
              </label>
              <input type="text" value={this.state.code} onChange={this.handleChangeCode} />
            </div>
          </div>
          <div className="row no-margin-left margin-bottom">
            <div className="large-6 columns">
              <label>
                Libellé <span className="required">*</span> :{" "}
              </label>
              <input type="text" value={this.state.libelle} onChange={this.handleChangeLibelle} />
            </div>
          </div>

          <div className="row no-margin-left margin-bottom">
            <div className="large-12 columns">
              <label>Variables : </label>
            </div>
            <div className="large-5 columns">
              <FilteredMultiSelect
                placeholder="Rechercher"
                buttonText="Ajouter"
                onChange={this.handleSelectionVariablesChange}
                options={options}
                selectedOptions={this.state.selectedOptions}
              />
            </div>
            <div className="large-5 columns">
              <FilteredMultiSelect
                placeholder="Rechercher"
                buttonText="Enlever"
                onChange={this.handleSelectedVariablesChange}
                options={this.state.selectedOptions}
              />
            </div>
          </div>

          <div className="row no-margin-left margin-bottom">
            <div className="large-12 columns">
              <div className="right">
                <input className="button boutonFichier" type="button" value="Valider" onClick={this.handleClickValider} />
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
    variables: visualisationReducer.variables
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
    setVariables: variables => {
      dispatch(setVariables(variables));
    },
    setDataflows: dataflows => {
      dispatch(setDataflows(dataflows));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DataflowVisualisation);
