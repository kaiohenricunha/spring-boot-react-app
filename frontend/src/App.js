// src/App.js
import React, { useState } from 'react';
import ProductList from './components/ProductList';
import ProductDetails from './components/ProductDetails';
import ProductForm from './components/ProductForm';

const App = () => {
    const [selectedProduct, setSelectedProduct] = useState(null);
    const [isEditing, setIsEditing] = useState(false);

    const handleProductSelect = (product) => {
        setSelectedProduct(product);
        setIsEditing(false);
    };

    const handleProductSaved = () => {
        setSelectedProduct(null);
        setIsEditing(false);
    };

    const handleAddNewProduct = () => {
        setSelectedProduct(null);
        setIsEditing(true);
    };

    return (
        <div className="app-container">
            <h1>Product Management</h1>
            <div className="content">
                <div className="sidebar">
                    <ProductList onSelectProduct={handleProductSelect} />
                    <button onClick={handleAddNewProduct}>Add New Product</button>
                </div>
                <div className="main-content">
                    {isEditing ? (
                        <ProductForm onProductSaved={handleProductSaved} existingProduct={selectedProduct} />
                    ) : (
                        <ProductDetails product={selectedProduct} />
                    )}
                </div>
            </div>
        </div>
    );
};

export default App;
