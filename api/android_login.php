<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

$response = ['success' => false, 'message' => ''];

$mobile = trim($_GET['mobile'] ?? '');
$password = trim($_GET['password'] ?? '');

if (empty($mobile)) {
    $response['message'] = 'Mobile required';
    echo json_encode($response);
    exit();
}

$server = "localhost\SQLEXPRESS";
$database = "MITALWEBDATA";
$uid = "mitesh";
$pwd = "Mital2025";

try {
    $conn = new PDO("sqlsrv:Server=$server;Database=$database", $uid, $pwd);
    
    $sql = "SELECT id, full_name, mobile FROM users 
            WHERE mobile = ? AND role = 'staff' AND status = 'active'";
    
    $stmt = $conn->prepare($sql);
    $stmt->execute([$mobile]);
    $user = $stmt->fetch(PDO::FETCH_ASSOC);
    
    if ($user) {
        $response['success'] = true;
        $response['message'] = 'Login successful';
        $response['user'] = [
            'id' => $user['id'],
            'name' => $user['full_name'],
            'mobile' => $user['mobile']
        ];
        
        $response['pending_count'] = 4;
        $response['pending_works'] = [
            ['party_name' => 'METRO PRINTER', 'work' => 'LAN SPEED PROBLEM'],
            ['party_name' => 'AASHIRWAD IND', 'work' => 'PC FT'],
            ['party_name' => 'NEON JEW', 'work' => 'MK 275 PROTER'],
            ['party_name' => 'DHARMA JEW', 'work' => 'CHIRAG BHAI']
        ];
    } else {
        $response['message'] = 'Staff not found';
    }
    
} catch(Exception $e) {
    $response['message'] = 'Database error';
}

echo json_encode($response, JSON_PRETTY_PRINT);
?>