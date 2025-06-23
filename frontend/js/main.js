document.addEventListener("DOMContentLoaded", () => {
    const categoryTable = document.getElementById("category-table");
    const productTable = document.getElementById("product-table");
    const categoryForm = document.getElementById("category-form");
    const productForm = document.getElementById("product-form");

    if (categoryTable) fetchCategories();
        loadFilterCategories();
    if (productTable) fetchProducts();
    if (categoryForm) categoryForm.addEventListener("submit", submitCategory);
    if (productForm) {
        loadCategoriesSelect();
        productForm.addEventListener("submit", submitProduct);
    }
});

function loadCategoriesSelect() {
    fetch("http://localhost:8080/category")
        .then(res => res.json())
        .then(categories => {
            const select = document.getElementById("category-select");
            if (!select) return;

            select.innerHTML = '<option value="">Select a category</option>';
            categories.forEach(cat => {
                const option = document.createElement("option");
                option.value = cat.id;
                option.textContent = cat.name;
                select.appendChild(option);
            });
        })
        .catch(err => console.error("Error loading categories:", err));
}

async function submitProduct(e) {
    e.preventDefault();
    const form = e.target;

    const data = {
        price: parseFloat(form.price.value),
        quantity: parseInt(form.quantity.value),
        category: {
            id: parseInt(form["category-select"].value)
        }
    };

    try {
        const response = await fetch("http://localhost:8080/product/create", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });

        const message = document.getElementById("response-message");
        message.textContent = response.ok
            ? "Product successfully created!"
            : "Error: " + (await response.text());

        if (response.ok) {
            form.reset();
            fetchProducts();
        }
    } catch (error) {
        console.error("Request error:", error);
    }
}

function fetchCategories() {
    fetch("http://localhost:8080/category")
        .then(response => response.json())
        .then(data => {
            const tbody = document.querySelector("#category-table tbody");
            if (!tbody) return;
            tbody.innerHTML = "";

            data.forEach(cat => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${cat.id}</td>
                    <td><input type="text" value="${cat.name}" id="name-${cat.id}" class="form-control" /></td>
                    <td>
                        <button class="btn btn-sm btn-primary me-2" onclick="updateCategory(${cat.id})">Save</button>
                        <button class="btn btn-sm btn-danger" onclick="deleteCategory(${cat.id})">Delete</button>
                    </td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(err => console.error("Error loading categories:", err));
}

async function submitCategory(e) {
    e.preventDefault();
    const form = e.target;
    const data = Object.fromEntries(new FormData(form).entries());

    try {
        const response = await fetch("http://localhost:8080/category/create", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });

        const message = document.getElementById("category-response-message");
        message.textContent = response.ok
            ? "Category successfully created!"
            : "Error: " + (await response.text());

        if (response.ok) {
            form.reset();
            fetchCategories();
            loadFilterCategories();
        }
    } catch (error) {
        console.error("Request error:", error);
    }
}

async function deleteCategory(id) {
    if (!confirm("Are you sure you want to delete this category?")) return;

    try {
        const response = await fetch(`http://localhost:8080/category/delete/${id}`, {
            method: "DELETE"
        });

        if (response.ok) {
            alert("Category successfully deleted!");
            fetchCategories();
            loadFilterCategories();
        } else {
            alert("Error deleting category.");
        }
    } catch (error) {
        console.error("Error deleting category:", error);
    }
}

async function updateCategory(id) {
    const name = document.getElementById(`name-${id}`).value;

    try {
        const response = await fetch(`http://localhost:8080/category/update/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name })
        });

        if (response.ok) {
            alert("Category successfully updated!");
        } else {
            alert("Error updating category.");
        }
    } catch (error) {
        console.error("Error updating category:", error);
    }
}

function fetchProducts() {
    fetch("http://localhost:8080/product")
        .then(response => response.json())
        .then(data => {
            const tbody = document.querySelector("#product-table tbody");
            if (!tbody) return;
            tbody.innerHTML = "";

            data.forEach(prod => {
                const categoryDisplay = prod.category?.name || prod.category?.id || "—";

                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${prod.id}</td>
                    <td>${prod.price}</td>
                    <td>${prod.quantity}</td>
                    <td>${categoryDisplay}</td>
                    <td>
                        <button class="btn btn-sm btn-danger" onclick="deleteProduct(${prod.id})">Delete</button>
                    </td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(err => console.error("Error loading products:", err));
}

async function deleteProduct(id) {
    if (!confirm("Are you sure you want to delete this product?")) return;

    try {
        const response = await fetch(`http://localhost:8080/product/delete/${id}`, {
            method: "DELETE"
        });

        if (response.ok) {
            alert("Product successfully deleted!");
            fetchProducts();
        } else {
            alert("Error deleting product.");
        }
    } catch (error) {
        console.error("Error deleting product:", error);
    }
}

async function fetchProductsByCategory(categoryId) {
    const url = categoryId === 'all'
        ? 'http://localhost:8080/product'
        : `http://localhost:8080/product/by-category/${categoryId}`;
    const response = await fetch(url);
    const products = await response.json();
    renderProducts(products);
}

function renderProducts(products) {
    const tbody = document.querySelector("#product-table tbody");
    if (!tbody) return;
    tbody.innerHTML = "";
    products.forEach(prod => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${prod.id}</td>
            <td>${prod.price}</td>
            <td>${prod.quantity}</td>
            <td>${prod.category?.name ?? ''}</td>
            <td>
                <button class="btn btn-sm btn-danger" onclick="deleteProduct(${prod.id})">Delete</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function loadFilterCategories() {
    const filter = document.getElementById("category-filter");
    if (!filter) return;

    fetch("http://localhost:8080/category")
        .then(response => response.json())
        .then(categories => {
            filter.innerHTML = '<option value="all">All Categories</option>';
            categories.forEach(cat => {
                const option = document.createElement("option");
                option.value = cat.id;
                option.textContent = cat.name;
                filter.appendChild(option);
            });

            filter.addEventListener("change", (e) => {
                fetchProductsByCategory(e.target.value);
            });

            fetchProductsByCategory("all"); // initial load
        })
        .catch(error => console.error("Error loading categories for filter:", error));
}
