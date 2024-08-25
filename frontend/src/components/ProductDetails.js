// src/components/ProductDetails.js
import React from 'react';

const ProductDetails = ({ product }) => {
    if (!product) {
        return <div>Select a product to see details</div>;
    }

    return (
        <div>
            <h2>Product Details</h2>
            <p><strong>Name:</strong> {product.name}</p>
            <p><strong>Description:</strong> {product.description}</p>
            <p><strong>Price:</strong> ${product.price.toFixed(2)}</p>
        </div>
    );
};

export default ProductDetails;
