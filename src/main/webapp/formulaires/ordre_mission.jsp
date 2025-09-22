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
        input[type="date"],
        select {
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
        input[type="date"]:focus,
        select:focus {
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
            display: inline-block;
            text-align: center;
        }

        .btn-submit:hover {
            background-color: #2c974b;
        }

        .btn-retour:hover {
            background-color: #0a58ca;
        }

        .radio-group {
            display: flex;
            gap: 16px;
            margin-top: 8px;
        }

        .radio-group label {
            display: flex;
            align-items: center;
            gap: 4px;
            font-weight: normal;
            margin-bottom: 0;
        }

        .error-message {
            color: #d93025;
            font-size: 12px;
            margin-top: 4px;
            display: none;
        }
    </style>
    <script>
        // Charger les primes selon le type de mission
        async function loadPrimes() {
            try {
                const typeMission = document.querySelector('input[name="typeMission"]:checked').value;

                // Sauvegarder la valeur actuelle
                const selectedValue = document.getElementById("fonction").value;

                const response = await fetch("<%=request.getContextPath()%>/PrimesServlet?type=" + typeMission);
                if (!response.ok) throw new Error('Erreur lors du chargement des primes');

                const primes = await response.json();
                let fonctionSelect = document.getElementById("fonction");
                fonctionSelect.innerHTML = "";

                let defaultOption = document.createElement("option");
                defaultOption.value = "";
                defaultOption.textContent = "Sélectionnez une fonction";
                fonctionSelect.appendChild(defaultOption);

                primes.forEach(p => {
                    let option = document.createElement("option");
                    option.value = p.fonction;
                    option.textContent = p.fonction;
                    option.dataset.prime = p.prime || 0;

                    // Restaurer la sélection si possible
                    if (p.fonction === selectedValue) {
                        option.selected = true;
                    }
                    fonctionSelect.appendChild(option);
                });

                // Mettre à jour montant si une option est encore sélectionnée
                updateMontant();

            } catch (error) {
                console.error("Erreur:", error);
                showError("Erreur lors du chargement des fonctions. Veuillez réessayer.");
            }
        }


        // Calcul durée automatiquement
        function calculerDuree() {
            const depart = document.getElementById("depart").value;
            const arrivee = document.getElementById("arrivee").value;

            if (depart && arrivee) {
                const dateDep = new Date(depart);
                const dateArr = new Date(arrivee);

                if (dateArr >= dateDep) {
                    const diffTime = dateArr - dateDep;
                    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); // inclusif
                    document.getElementById("duree").value = diffDays;
                } else {
                    showError("La date d'arrivée doit être postérieure ou égale à la date de départ");
                    document.getElementById("duree").value = 0;
                }
                updateMontant();
            }
        }

        // Mettre à jour les montants
        function updateMontant() {
            try {
                let selectedOption = document.getElementById("fonction").selectedOptions[0];
                let primeValue = selectedOption.dataset.prime || "0";
                let primeJour = parseFloat(primeValue);
                if (isNaN(primeJour)) primeJour = 0;

                let duree = parseInt(document.getElementById("duree").value) || 0;

                // Devise selon type mission
                const typeMission = document.querySelector('input[name="typeMission"]:checked').value;
                const devise = (typeMission === "interieur") ? "MRU" : "€";

                document.getElementById("labelMontantJour").textContent = "Montant par jour (" + devise + ")";
                document.getElementById("labelMontantTotal").textContent = "Montant total (" + devise + ")";

                document.getElementById("montantJour").value = primeJour.toFixed(0);

                if (duree > 0 && primeJour > 0) {
                    document.getElementById("montantTotal").value = (primeJour * duree).toFixed(0);
                } else {
                    document.getElementById("montantTotal").value = "0";
                }
            } catch (error) {
                console.error("Erreur dans updateMontant:", error);
                document.getElementById("montantJour").value = "0.00";
                document.getElementById("montantTotal").value = "0.00";
            }
        }

        function showError(message) {
            const errorDiv = document.getElementById("error-message");
            errorDiv.textContent = message;
            errorDiv.style.display = "block";
            setTimeout(() => {
                errorDiv.style.display = "none";
            }, 5000);
        }

        window.onload = function () {
            loadPrimes();
            document.getElementById("fonction").addEventListener("change", updateMontant);
            document.getElementById("depart").addEventListener("change", calculerDuree);
            document.getElementById("arrivee").addEventListener("change", calculerDuree);

            const radioButtons = document.querySelectorAll('input[name="typeMission"]');
            radioButtons.forEach(radio => {
                radio.addEventListener("change", function () {
                    loadPrimes();
                    document.getElementById("montantJour").value = "0.00";
                    document.getElementById("montantTotal").value = "0.00";
                    updateMontant();
                });
            });

            document.getElementById("montantJour").value = "0.00";
            document.getElementById("montantTotal").value = "0.00";
        };
    </script>
</head>
<body>
<div class="container">
    <h2>Formulaire Ordre de Mission</h2>
    <div id="error-message" class="error-message"></div>
    <form action="${pageContext.request.contextPath}/GenerateOrdreMission" method="post">
        <div class="form-row">
            <div class="form-group half">
                <label for="nom">Nom et Prénom</label>
                <input type="text" id="nom" name="nom" required>
            </div>
            <div class="form-group half">
                <label for="fonction">Fonction</label>
                <select id="fonction" name="fonction" required></select>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group half">
                <label>Type de mission</label>
                <div class="radio-group">
                    <label><input type="radio" name="typeMission" value="interieur" checked> Intérieur</label>
                    <label><input type="radio" name="typeMission" value="etranger"> Étranger</label>
                </div>
            </div>
            <div class="form-group half">
                <label id="labelMontantJour" for="montantJour">Montant par jour (MRU)</label>
                <input type="text" id="montantJour" name="montantJour" readonly required>
            </div>
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
                <label for="duree">Durée (jours)</label>
                <input type="number" id="duree" name="duree" readonly required>
            </div>
            <div class="form-group half">
                <label id="labelMontantTotal" for="montantTotal">Montant total (MRU)</label>
                <input type="text" id="montantTotal" name="montantTotal" readonly>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group half">
                <label for="nationalite">Nationalité</label>
                <input type="text" id="nationalite" name="nationalite" required>
            </div>
            <div class="form-group half">
                <label for="compagnie">Mode de transport</label>
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
                <label for="faitA">Fait à</label>
                <input type="text" id="faitA" name="faitA" required>
            </div>
        </div>

        <div class="buttons-container">
            <button type="submit" class="btn-submit">Générer PDF</button>
            <a href="../index.jsp" class="btn-retour">Retour</a>
        </div>
    </form>
</div>
</body>
</html>