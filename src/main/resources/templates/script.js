let inventoryList = [
  { Id: 35, name: 'Amoxacillin', rQuantity: 10 },
  { Id: 36, name: 'Paracetamol', rQuantity: 20 },
  // ... potentially more items
];

// Function to render the table
function renderTable(data) {
  const tableBody = document.getElementById('inventoryBody');
  tableBody.innerHTML = '';

  data.forEach((item) => {
    const row = tableBody.insertRow();

    // Fill in the row with cell values
    Object.values(item).forEach((value) => {
      const cell = row.insertCell();
      cell.textContent = value;
          });

          // Add an edit button to each row
          const editCell = row.insertCell();
          editCell.innerHTML = `<button onclick="editItem(${item.Id})">Edit</button>`;
        });
      }

      // Function to filter the table based on name
      function filterByName(name) {
        const filteredData = inventoryList.filter((item) =>
          item.name.toLowerCase().includes(name.toLowerCase())
        );
        renderTable(filteredData);
      }

      // Function to edit an item
      function editItem(Id) {
        // Implement the logic to edit an item based on its ID
        // For example:
        console.log(`Editing item with Id: ${id}`);
      }

      // Initial rendering of the table
      renderTable(inventoryList);