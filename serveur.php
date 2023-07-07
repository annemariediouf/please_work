<?php

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username']; 
    $password = $_POST['password']; 

   
    if ($password === "work") { 
        $response = array('status' => 'success', 'message' => 'Connexion réussie !');
    } else {
        $response = array('status' => 'error', 'message' => 'Connexion échouée !');
    }

    
    header('Content-Type: application/json');
    echo json_encode($response);
}
?>
