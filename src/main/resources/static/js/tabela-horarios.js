console.log('DOM carregado');
document.addEventListener('DOMContentLoaded', function() {
    const selectedCells = new Set();
    const horariosInput = document.getElementById('horarios');

    document.querySelectorAll('.selectable').forEach(cell => {
        cell.addEventListener('click', function() {
            const cellId = `${this.dataset.day}-${this.dataset.time}`;
            if (selectedCells.has(cellId)) {
                selectedCells.delete(cellId);
                this.classList.remove('table-primary');
            } else {
                selectedCells.add(cellId);
                this.classList.add('table-primary');
            }
            horariosInput.value = Array.from(selectedCells).join(',');
        });
    });
});