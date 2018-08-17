import React from "react";
import { connect } from "react-redux";
import ReactTable from "react-table";
import "react-table/react-table.css";
import { FilAriane } from "../../commun/FilAriane";
import { initPost, initPostFile } from "../../_utils";
import { WS_CONTEXT_PATH } from "../../_properties";
import { waitingTrue, waitingFalse } from "../../commun/GeneralReducer";
import DSDOnglets from "./DSDOnglets";
import { fetchConcepts, fetchConceptSchemes } from "../ReferentielReducer";
import { setDSD } from "../ChargementReducer";

class DSDCreerAttributs extends React.Component {
  constructor(props) {
    super(props);
    this;
  }

  componentDidMount() {
    this.props.fetchConcepts();
    this.props.fetchConceptSchemes();
  }

  onClickAjouter = event => {
    this.props.dsd.attributs.push({});
    this.props.setDSD(this.props.dsd);
  };

  handleChangeConcept = (index, event) => {
    const { concepts, conceptSchemes } = this.props;
    const cs = conceptSchemes.find(concept => concept.iri === this.props.dsd.attributs[index].iriConceptScheme);
    this.props.dsd.attributs[index].iriConcept = concepts[event.target.value].iri;
    if (cs) {
      this.props.dsd.attributs[index].iri = concepts[event.target.value].code + "/" + cs.code;
      this.props.dsd.attributs[index].libelleFr = concepts[event.target.value].libelleFr + " - " + cs.libelleFr;
      this.props.dsd.attributs[index].libelleEn = concepts[event.target.value].libelleEn + " - " + cs.libelleEn;
    }
    this.props.setDSD(this.props.dsd);
  };

  handleChangeConceptScheme = (index, event) => {
    const { concepts, conceptSchemes } = this.props;
    const concept = concepts.find(concept => concept.iri === this.props.dsd.attributs[index].iriConcept);
    this.props.dsd.attributs[index].iriConceptScheme = conceptSchemes[event.target.value].iri;
    this.props.dsd.attributs[index].iriRange = conceptSchemes[event.target.value].iriClass;
    if (concept) {
      this.props.dsd.attributs[index].iri = concept.code + "/" + conceptSchemes[event.target.value].code;
      this.props.dsd.attributs[index].libelleFr = concept.libelleFr + " - " + conceptSchemes[event.target.value].libelleFr;
      this.props.dsd.attributs[index].libelleEn = concept.libelleEn + " - " + conceptSchemes[event.target.value].libelleEn;
    }
    this.props.setDSD(this.props.dsd);
  };

  handleChangeAttachement = (index, event) => {
    this.props.dsd.attributs[index].iriAttachment = event.target.value;
    this.props.setDSD(this.props.dsd);
  };

  onClickSupprimer = event => {
    this.props.dsd.attributs = this.props.dsd.attributs.filter(attribut => attribut.iri !== event.target.name);
    this.props.setDSD(this.props.dsd);
  };

  render() {
    const { dsd, concepts, conceptSchemes, context } = this.props;
    const columns = [
      {
        Header: "Concept",
        accessor: "iriConcept",
        Cell: props => {
          const listeConcepts = concepts.map((concept, index) => {
            if (concept.iri === props.value) {
              return (
                <option selected="selected" value={index}>
                  {concept.libelleFr}
                </option>
              );
            } else {
              return <option value={index}>{concept.libelleFr}</option>;
            }
          });
          return (
            <div className="center">
              <select onChange={e => this.handleChangeConcept(props.index, e)}>
                <option> </option>
                {listeConcepts}
              </select>
            </div>
          );
        }
      },

      {
        Header: "Code liste",
        accessor: "iriConceptScheme",
        Cell: props => {
          const listeConceptSchemes = conceptSchemes.map((concept, index) => {
            if (concept.iri === props.value) {
              return (
                <option selected="selected" value={index}>
                  {concept.libelleFr}
                </option>
              );
            } else {
              return <option value={index}>{concept.libelleFr}</option>;
            }
          });
          return (
            <div className="center">
              <select onChange={e => this.handleChangeConceptScheme(props.index, e)}>
                <option> </option>
                {listeConceptSchemes}
              </select>
            </div>
          );
        }
      },
      {
        Header: "Rattachement",
        accessor: "iriAttachment",
        Cell: props => {
          const attachments = [
            {
              iri: "http://purl.org/linked-data/cube#Slice",
              libelle: "Slice"
            },
            {
              iri: "http://purl.org/linked-data/cube#Observation",
              libelle: "Observation"
            }
          ];
          const listeAttachements = attachments.map(attachment => {
            if (attachment.iri === props.value) {
              return (
                <option selected="selected" value={attachment.iri}>
                  {attachment.libelle}
                </option>
              );
            } else {
              return <option value={attachment.iri}>{attachment.libelle}</option>;
            }
          });
          return (
            <div className="center">
              <select onChange={e => this.handleChangeAttachement(props.index, e)}>
                <option> </option>
                {listeAttachements}
              </select>
            </div>
          );
        }
      },
      {
        accessor: "iri",
        width: 80,
        Cell: props => (
          <div className="center">
            <img onClick={this.onClickSupprimer} name={props.value} className="icone" src={context + "/static/icons/trash.svg"} alt="Supprimer" />
          </div>
        )
      }
    ];
    return (
      <div>
        <DSDOnglets dsdParams={this.props.match.params.dsd} active={5} />
        <div className="tab-pane">
          <div className="row margin-bottom">
            <div className="large-12 columns">
              <label>Liste des attributs : </label>
            </div>
          </div>
          <div className="row margin-bottom">
            <ReactTable defaultPageSize={10} minRows={0} data={dsd.attributs} columns={columns} />
          </div>
          <div className="row margin-bottom">
            <div className="large-10 columns" />
            <div className="large-2 columns">
              <input type="submit" value="+" onClick={this.onClickAjouter} />
            </div>
          </div>
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
)(DSDCreerAttributs);
