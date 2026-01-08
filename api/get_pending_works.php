<?php
// api/get_pending_works.php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

$response = ['success' => true, 'message' => 'Pending works'];

// Demo data
$response['pending_count'] = 4;
$response['works'] = [
    [
        'work_id' => '#41',
        'party_name' => 'METRO PRINTER',
        'work_name' => 'LAN SPEED PROBLEM',
        'work_type' => 'NETWORK_LAM',
        'date' => '08-01-2026',
        'status' => 'Pending'
    ],
    [
        'work_id' => '#40',
        'party_name' => 'AASHIRWAD IND PARTH CRE',
        'work_name' => 'PC FT',
        'work_type' => 'COMPUTER',
        'date' => '08-01-2026',
        'status' => 'Pending'
    ],
    [
        'work_id' => '#39',
        'party_name' => 'NEON JEW',
        'work_name' => 'MK 275 PROTER',
        'work_type' => 'COMPUTER',
        'date' => '08-01-2026',
        'status' => 'Pending'
    ],
    [
        'work_id' => '#37',
        'party_name' => 'DHARMA JEW',
        'work_name' => 'CHIRAG BHAI',
        'work_type' => 'COMPUTER',
        'date' => '08-01-2026',
        'status' => 'Pending'
    ]
];

echo json_encode($response, JSON_PRETTY_PRINT);
?>