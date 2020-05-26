package adri.suys.un_mutescan.presenter;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import adri.suys.un_mutescan.model.Counterpart;
import adri.suys.un_mutescan.model.Event;
import adri.suys.un_mutescan.utils.UnMuteDataHolder;
import adri.suys.un_mutescan.viewinterfaces.EventListViewInterface;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventPresenterTest {

    @Mock EventListViewInterface viewInterface;
    EventPresenter presenter;
    private JSONArray jsonArrayOK;
    private JSONArray jsonArrayKO;

    @Before
    public void setUp() throws Exception {
        String responseOK = getResponseFromAPI();
        String responseKO = "[\n" +
                "    {\n" +
                "        \"error\": \"Vous n'avez pas d'événements.\"\n" +
                "    }\n" +
                "]";
        jsonArrayOK = new JSONArray(responseOK);
        jsonArrayKO = new JSONArray(responseKO);
        presenter = new EventPresenter(viewInterface);
    }

    @Test
    public void handleJSONArrayTestSuccess(){
        presenter.handleJSONArray(jsonArrayOK);
        verify(viewInterface).backUpEvents();
    }

    @Test
    public void handleJSONArrayTestFail(){
        presenter.handleJSONArray(jsonArrayKO);
        assertTrue(presenter.getError() != "");
    }

    private String getResponseFromAPI(){
        return "[\n" +
                "    {\n" +
                "        \"id\": 750041,\n" +
                "        \"name\": \"[Ticked-it] Event B\",\n" +
                "        \"nbTotalTicket\": 550,\n" +
                "        \"nbScannedTicket\": 0,\n" +
                "        \"nbSoldTicket\": 4,\n" +
                "        \"nbBoughtOnSiteTicket\": 0,\n" +
                "        \"date\": {\n" +
                "            \"date\": \"2019-05-06 20:00:00.000000\",\n" +
                "            \"timezone_type\": 3,\n" +
                "            \"timezone\": \"Europe/Brussels\"\n" +
                "        },\n" +
                "        \"nbTicketBoughtInCash\": 0,\n" +
                "        \"error\": \"\",\n" +
                "        \"audience\": [],\n" +
                "        \"counterparts\": [\n" +
                "            {\n" +
                "                \"id\": 78,\n" +
                "                \"name\": \"Ticket Fosse\",\n" +
                "                \"price\": 30\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 79,\n" +
                "                \"name\": \"Ticket Assis Adulte\",\n" +
                "                \"price\": 35\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 80,\n" +
                "                \"name\": \"Ticket VIP\",\n" +
                "                \"price\": 50\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 82,\n" +
                "                \"name\": \"Ticket Assis Enfant\",\n" +
                "                \"price\": 30\n" +
                "            }\n" +
                "        ],\n" +
                "        \"detailsTixPerCp\": [\n" +
                "            {\n" +
                "                \"78\": 1\n" +
                "            },\n" +
                "            {\n" +
                "                \"79\": 1\n" +
                "            },\n" +
                "            {\n" +
                "                \"80\": 1\n" +
                "            },\n" +
                "            {\n" +
                "                \"82\": 1\n" +
                "            }\n" +
                "        ],\n" +
                "        \"photoPath\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 750039,\n" +
                "        \"name\": \"[Ticked-it] Event A\",\n" +
                "        \"nbTotalTicket\": 500,\n" +
                "        \"nbScannedTicket\": 0,\n" +
                "        \"nbSoldTicket\": 0,\n" +
                "        \"nbBoughtOnSiteTicket\": 0,\n" +
                "        \"date\": {\n" +
                "            \"date\": \"2019-06-20 20:00:00.000000\",\n" +
                "            \"timezone_type\": 3,\n" +
                "            \"timezone\": \"Europe/Brussels\"\n" +
                "        },\n" +
                "        \"nbTicketBoughtInCash\": 0,\n" +
                "        \"error\": \"\",\n" +
                "        \"audience\": [],\n" +
                "        \"counterparts\": [\n" +
                "            {\n" +
                "                \"id\": 76,\n" +
                "                \"name\": \"Ticket adulte\",\n" +
                "                \"price\": 10\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 77,\n" +
                "                \"name\": \"Ticket enfant\",\n" +
                "                \"price\": 8\n" +
                "            }\n" +
                "        ],\n" +
                "        \"detailsTixPerCp\": [\n" +
                "            {\n" +
                "                \"76\": 0\n" +
                "            },\n" +
                "            {\n" +
                "                \"77\": 0\n" +
                "            }\n" +
                "        ],\n" +
                "        \"photoPath\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 750051,\n" +
                "        \"name\": \"[Ticked-it] Réunion Festival 14 juin\",\n" +
                "        \"nbTotalTicket\": 50,\n" +
                "        \"nbScannedTicket\": 6,\n" +
                "        \"nbSoldTicket\": 14,\n" +
                "        \"nbBoughtOnSiteTicket\": 29,\n" +
                "        \"date\": {\n" +
                "            \"date\": \"2019-05-17 16:41:35.528103\",\n" +
                "            \"timezone_type\": 3,\n" +
                "            \"timezone\": \"Europe/Brussels\"\n" +
                "        },\n" +
                "        \"nbTicketBoughtInCash\": 29,\n" +
                "        \"error\": \"\",\n" +
                "        \"audience\": [\n" +
                "            {\n" +
                "                \"buyer\": \"Michele Martin\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"Placement libre\",\n" +
                "                \"barcode\": \"cf9835cc6e03d651421\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"Jean Petit\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"Placement libre\",\n" +
                "                \"barcode\": \"cf9845cc6eb34094d61\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"emile louis\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"Placement libre\",\n" +
                "                \"barcode\": \"cf9855cc6efcd814581\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"Bob Eponge\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"Placement libre\",\n" +
                "                \"barcode\": \"cf9865cc6f1f52a9121\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"Jean Paul\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"Placement libre\",\n" +
                "                \"barcode\": \"cf9875cc6fa25a078e1\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"Marie Jesus\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"Placement libre\",\n" +
                "                \"barcode\": \"cf9885cc6fbe1981461\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"faux\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"Jennyfer Jarrow\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"Placement libre\",\n" +
                "                \"barcode\": \"cf9895cc6fe779babf1\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"faux\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"Pierre Blackman\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"Placement libre\",\n" +
                "                \"barcode\": \"cf9905cc701620453c1\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"faux\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"jon doe\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"Placement libre\",\n" +
                "                \"barcode\": \"cf9915cc702b22d4d31\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"faux\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"ynns pakis\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"Placement libre\",\n" +
                "                \"barcode\": \"cf9925cc70391add1e1\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"faux\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"jimmy smith\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"Placement libre\",\n" +
                "                \"barcode\": \"cf9935cc7057bdb5821\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"jimmy smith\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"Placement libre\",\n" +
                "                \"barcode\": \"cf9935cc7057bdb5821\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"faux\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"jimmy smith\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"Placement libre\",\n" +
                "                \"barcode\": \"cf9935cc7057bdb5821\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"faux\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"jimmy smith\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"Placement libre\",\n" +
                "                \"barcode\": \"cf9935cc7057bdb5821\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"faux\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"Tyrion Lannister\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \" \",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"anonyme\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"buyer\": \"Guillaume Denis\",\n" +
                "                \"ticket_type\": \"Jeton de présence\",\n" +
                "                \"seat_type\": \"N/A\",\n" +
                "                \"barcode\": \"0\",\n" +
                "                \"error\": \"\",\n" +
                "                \"is_validated\": \"vrai\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"counterparts\": [\n" +
                "            {\n" +
                "                \"id\": 86,\n" +
                "                \"name\": \"Jeton de présence\",\n" +
                "                \"price\": 5\n" +
                "            }\n" +
                "        ],\n" +
                "        \"detailsTixPerCp\": [\n" +
                "            {\n" +
                "                \"86\": 43\n" +
                "            }\n" +
                "        ],\n" +
                "        \"photoPath\": \"fred-and-the-healers-spacium.jpg\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 750049,\n" +
                "        \"name\": \"[Ticked-it] Una evenementa spectacular\",\n" +
                "        \"nbTotalTicket\": 403,\n" +
                "        \"nbScannedTicket\": 1,\n" +
                "        \"nbSoldTicket\": 8,\n" +
                "        \"nbBoughtOnSiteTicket\": 0,\n" +
                "        \"date\": {\n" +
                "            \"date\": \"2019-05-28 12:00:00.000000\",\n" +
                "            \"timezone_type\": 3,\n" +
                "            \"timezone\": \"Europe/Brussels\"\n" +
                "        },\n" +
                "        \"nbTicketBoughtInCash\": 0,\n" +
                "        \"error\": \"\",\n" +
                "        \"audience\": [],\n" +
                "        \"counterparts\": [\n" +
                "            {\n" +
                "                \"id\": 85,\n" +
                "                \"name\": \"Uno ticketo normale\",\n" +
                "                \"price\": 34\n" +
                "            }\n" +
                "        ],\n" +
                "        \"detailsTixPerCp\": [\n" +
                "            {\n" +
                "                \"85\": 8\n" +
                "            }\n" +
                "        ],\n" +
                "        \"photoPath\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 750048,\n" +
                "        \"name\": \"[Ticked-it] Event C\",\n" +
                "        \"nbTotalTicket\": 400,\n" +
                "        \"nbScannedTicket\": 0,\n" +
                "        \"nbSoldTicket\": 0,\n" +
                "        \"nbBoughtOnSiteTicket\": 0,\n" +
                "        \"date\": {\n" +
                "            \"date\": \"2019-05-24 20:30:00.000000\",\n" +
                "            \"timezone_type\": 3,\n" +
                "            \"timezone\": \"Europe/Brussels\"\n" +
                "        },\n" +
                "        \"nbTicketBoughtInCash\": 0,\n" +
                "        \"error\": \"\",\n" +
                "        \"audience\": [],\n" +
                "        \"counterparts\": [\n" +
                "            {\n" +
                "                \"id\": 84,\n" +
                "                \"name\": \"Ticket général\",\n" +
                "                \"price\": 5\n" +
                "            }\n" +
                "        ],\n" +
                "        \"detailsTixPerCp\": [\n" +
                "            {\n" +
                "                \"84\": 0\n" +
                "            }\n" +
                "        ],\n" +
                "        \"photoPath\": \"\"\n" +
                "    }\n" +
                "]";
    }

    public boolean hasEvent(List<Event> events, int id){
        for (Event e : events) {
            if (e.getId() == id){
                return true;
            }
        }
        return false;
    }
}