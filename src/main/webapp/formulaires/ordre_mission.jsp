<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ordre de Mission</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Helvetica, Arial, sans-serif;
        }

        body {
            padding: 20px;
            background-color: #f6f8fa;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            background-color: #fff;
            padding: 24px;
            border-radius: 6px;
            border: 1px solid #d0d7de;
        }

        h2 {
            margin-bottom: 24px;
            color: #24292f;
            font-size: 24px;
            border-bottom: 1px solid #d0d7de;
            padding-bottom: 16px;
        }

        .form-row {
            display: flex;
            gap: 20px;
            margin-bottom: 16px;
        }

        .form-group {
            margin-bottom: 16px;
        }

        .form-group.half {
            flex: 1;
            margin-bottom: 0;
        }

        .form-group.full {
            width: 100%;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
            color: #24292f;
        }

        input[type="text"],
        input[type="number"],
        input[type="date"] {
            width: 100%;
            padding: 8px 12px;
            border: 1px solid #d0d7de;
            border-radius: 6px;
            font-size: 14px;
            line-height: 20px;
            color: #24292f;
            background-color: #f6f8fa;
        }

        input[type="text"]:focus,
        input[type="number"]:focus,
        input[type="date"]:focus {
            outline: none;
            border-color: #0969da;
            box-shadow: 0 0 0 3px rgba(9, 105, 218, 0.3);
        }

        .buttons-container {
            display: flex;
            gap: 12px;
            margin-top: 24px;
        }

        .btn-submit {
            padding: 8px 16px;
            background-color: #2da44e;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 500;
        }

        .btn-retour {
            padding: 8px 16px;
            background-color: #0969da;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            text-decoration: none;
            font-size: 14px;
            font-weight: 500;
        }

        .btn-submit:hover {
            background-color: #2c974b;
        }

        .btn-retour:hover {
            background-color: #0a58ca;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Formulaire Ordre de Mission</h2>
    <form action="${pageContext.request.contextPath}/GenerateOrdreMission" method="post">
        <div class="form-row">
            <div class="form-group half">
                <label for="nom">Nom et Prénom</label>
                <input type="text" id="nom" name="nom" required>
            </div>
            <div class="form-group half">
                <label for="fonction">Fonction</label>
                <input type="text" id="fonction" name="fonction" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group half">
                <label for="nationalite">Nationalité</label>
                <input type="text" id="nationalite" name="nationalite" required>
            </div>
            <div class="form-group half">
                <label for="compagnie">Compagnie</label>
                <input type="text" id="compagnie" name="compagnie">
            </div>
        </div>

        <div class="form-group full">
            <label for="objet">Objet de la mission</label>
            <input type="text" id="objet" name="objet" required>
        </div>

        <div class="form-group full">
            <label for="itineraire">Itinéraire</label>
            <input type="text" id="itineraire" name="itineraire">
        </div>

        <div class="form-row">
            <div class="form-group half">
                <label for="depart">Date de départ</label>
                <input type="date" id="depart" name="depart" required>
            </div>
            <div class="form-group half">
                <label for="arrivee">Date d'arrivée</label>
                <input type="date" id="arrivee" name="arrivee" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group half">
                <label for="montantJour">Montant par jour (MRU)</label>
                <input type="number" id="montantJour" name="montantJour" required>
            </div>
            <div class="form-group half">
                <label for="duree">Durée (jours)</label>
                <input type="number" id="duree" name="duree" required>
            </div>
        </div>

        <div class="form-group half">
            <label for="faitA">Fait à</label>
            <input type="text" id="faitA" name="faitA" required>
        </div>

        <div class="buttons-container">
            <button type="submit" class="btn-submit">Générer PDF</button>
            <a href="../index.jsp" class="btn-retour">Retour</a>
        </div>
    </form>
</div>
</body>
</html>